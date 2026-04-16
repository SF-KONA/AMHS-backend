import os
import json
import pymysql
import random
from datetime import datetime

# 1. Aiven Cloud DB 접속 정보
db_config = {
    'host': 'mysql-35417ff9-predictive-factory-db.e.aivencloud.com',
    'port': 22411,
    'user': 'avnadmin',
    'password': '**********',
    'database': 'amhs_db',
    'charset': 'utf8mb4',
    'ssl': {'ca': './ca.pem'}
}

# 2. 시스템 구성 및 작업자 설정
installers = ['김1', '김2', '김3', '박1', '박2']
fabs = [
    {'id': 'FAB-A', 'name': '제1 공장'},
    {'id': 'FAB-B', 'name': '제2 공장'}
]
lines = [
    {'id': 'LINE-1', 'fab_id': 'FAB-A', 'type': 'OHT'},
    {'id': 'LINE-2', 'fab_id': 'FAB-A', 'type': 'AGV'},
    {'id': 'LINE-3', 'fab_id': 'FAB-B', 'type': 'OHT'},
    {'id': 'LINE-4', 'fab_id': 'FAB-B', 'type': 'AGV'}
]

def migrate():
    base_dir = './02.라벨링데이터'
    batch_size = 500
    val_list = []
    total_count = 0

    conn = pymysql.connect(**db_config)
    try:
        with conn.cursor() as cursor:
            print("Aiven 클라우드 마스터 정보 등록 중...")
            for f in fabs:
                cursor.execute("INSERT IGNORE INTO fab (fab_id, fab_name) VALUES (%s, %s)", (f['id'], f['name']))
            for l in lines:
                simple_line_name = f"Line {l['id'].split('-')[-1]}"
                cursor.execute("""
                    INSERT IGNORE INTO production_line (line_id, line_name, device_type, total_slots, min_running, fab_id) 
                    VALUES (%s, %s, %s, %s, %s, %s)
                """, (l['id'], simple_line_name, l['type'], 6, 7, l['fab_id']))
            conn.commit()

            # --- [단계 2] 전수 조사 및 클라우드 적재 (동일) ---
            print("모든 데이터를 찾아 Aiven으로 전송을 시작합니다...")
            for root, dirs, files in os.walk(base_dir):
                json_files = [f for f in files if f.endswith('.json')]
                for file_name in json_files:
                    try:
                        with open(os.path.join(root, file_name), 'r', encoding='utf-8') as f:
                            data = json.load(f)
                            
                            meta = data['meta_info'][0]
                            s_data = data['sensor_data'][0]
                            ir = data['ir_data'][0]['temp_max'][0]
                            ann = data['annotations'][0]['tagging'][0]
                            ext = data['external_data'][0]

                            device_id = meta['device_id'].lower()
                            device_type = 'AGV' if 'agv' in device_id else 'OHT'
                            
                            target_lines = [l for l in lines if l['type'] == device_type]
                            assigned_line = target_lines[total_count % len(target_lines)]

                            assigned_installer = random.choice(installers)
                            cursor.execute("""
                                INSERT IGNORE INTO device (
                                    device_id, device_manufacturer, device_name, device_type,
                                    installer, cumulative_operating_day, fab_id, line_id
                                ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s)
                            """, (
                                device_id, meta['device_manufacturer'], meta['device_name'], device_type,
                                assigned_installer, int(meta['cumulative_operating_day']),
                                assigned_line['fab_id'], assigned_line['id']
                            ))

                            collected_at = datetime.strptime(f"2024-{meta['collection_date']} {meta['collection_time']}", '%Y-%m-%d %H:%M:%S')

                            val_list.append((
                                collected_at,
                                s_data['PM10'][0]['value'], s_data['PM2.5'][0]['value'], s_data['PM1.0'][0]['value'], 
                                s_data['NTC'][0]['value'],
                                s_data['CT1'][0]['value'], s_data['CT2'][0]['value'], s_data['CT3'][0]['value'], s_data['CT4'][0]['value'],
                                ir['value_TGmx'], ir['X_Tmax'], ir['Y_Tmax'],
                                int(ann['state']),
                                ext['ex_temperature'][0]['value'], ext['ex_humidity'][0]['value'], ext['ex_illuminance'][0]['value'],
                                meta['img_name'], device_id
                            ))
                            total_count += 1

                            if len(val_list) >= batch_size:
                                sql = """
                                INSERT INTO sensor_reading (
                                    collected_at, pm10, pm2p5, pm1p0, ntc,
                                    ct1, ct2, ct3, ct4, ir_temp_max, x_tmax, y_tmax,
                                    state, ex_temp, ex_humidity, ex_lux,
                                    ir_image_path, device_id
                                ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
                                """
                                cursor.executemany(sql, val_list)
                                conn.commit()
                                if total_count % 1000 == 0:
                                    print(f"클라우드 적재 진행 중: {total_count}개 완료...")
                                val_list = []

                    except Exception as e:
                        print(f"파일 오류 ({file_name}): {e}")

            if val_list:
                cursor.executemany(sql, val_list)
                conn.commit()

            print(f"[성공] {total_count}개의 데이터가 심플한 라인 이름과 함께 Aiven에 저장되었습니다")

    except Exception as e:
        print(f"연결 실패: {e}")
    finally:
        conn.close()

if __name__ == "__main__":
    migrate()