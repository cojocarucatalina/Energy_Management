import pika

cloudamqp_url = "amqps://qzwqpbut:C9czddqHxrh-cLELUuCiiFgesCdbCU-G@rat.rmq2.cloudamqp.com/qzwqpbut"

params = pika.URLParameters(cloudamqp_url)
connection = pika.BlockingConnection(params)
channel = connection.channel()

queue_name = 'measurements'
channel.queue_declare(queue=queue_name, durable=True)

def callback(ch, method, properties, body):
    print(f"Received message: {body.decode('utf-8')}")
    ch.basic_ack(delivery_tag=method.delivery_tag)

# Set up consumption
channel.basic_qos(prefetch_count=1)  # Fair dispatch
channel.basic_consume(queue=queue_name, on_message_callback=callback)

print("Waiting for messages. To exit press CTRL+C")
channel.start_consuming()
