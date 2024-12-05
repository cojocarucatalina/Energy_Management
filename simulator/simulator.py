# import csv
# import time
# import pika
#
# cloudamqp_url = "amqps://qzwqpbut:C9czddqHxrh-cLELUuCiiFgesCdbCU-G@rat.rmq2.cloudamqp.com/qzwqpbut"
#
# params = pika.URLParameters(cloudamqp_url)
# connection = pika.BlockingConnection(params)
# channel = connection.channel()
#
# queue_name = 'measurements'
# channel.queue_declare(queue=queue_name, durable=True)
#
# csv_file_path = 'test.csv'
#
# try:
#     with open(csv_file_path, mode='r', newline='', encoding='utf-8') as csvfile:
#         csv_reader = csv.reader(csvfile)
#
#         count = 1
#         for row in csv_reader:
#             message = ','.join(row)
#
#             channel.basic_publish(
#                 exchange='',
#                 routing_key=queue_name,
#                 body=message,
#                 properties=pika.BasicProperties(
#                     delivery_mode=2,
#                 )
#             )
#
#             print(f"Sent Row {count}: {message}")
#             count += 1
#             time.sleep(1)
#
# except Exception as e:
#     print(f"An error occurred: {e}")
#
# finally:
#     # Close the connection
#     connection.close()
#     print("Connection closed.")
import csv
import time
import json
import pika
from datetime import datetime, timezone, timedelta

cloudamqp_url = "amqps://qzwqpbut:C9czddqHxrh-cLELUuCiiFgesCdbCU-G@rat.rmq2.cloudamqp.com/qzwqpbut"
params = pika.URLParameters(cloudamqp_url)
connection = pika.BlockingConnection(params)
channel = connection.channel()

queue_name = 'measurements'
channel.queue_declare(queue=queue_name, durable=True)

config_file_path = 'config.config'
sensor_file_path = 'test.csv'

try:
    with open(config_file_path, mode='r', encoding='utf-8') as config_file:
        device_id = config_file.read().strip()

    with open(sensor_file_path, mode='r', newline='', encoding='utf-8') as csvfile:
        csv_reader = csv.reader(csvfile)

        count = 1
        for row in csv_reader:
            measurement_value = float(row[0])
            message = {
                "timestamp": int(datetime.now(timezone.utc).timestamp() * 1000),
                "device_id": device_id,
                "measurement_value": measurement_value,
            }

            message_json = json.dumps(message)

            channel.basic_publish(
                exchange='',
                routing_key=queue_name,
                body=message_json,
                properties=pika.BasicProperties(
                    delivery_mode=2,
                )
            )

            print(f"Sent Row {count}: {message_json}")
            count += 1

            time.sleep(5)

except Exception as e:
    print(f"An error occurred: {e}")

finally:
    connection.close()
    print("Connection closed.")

