nohup java -Xms21m -Xmx64m -jar ./id-region-0.0.1-SNAPSHOT.jar --server.port=8227 --nodelist=127.0.0.1:8225,127.0.0.1:8226,127.0.0.1:8227 > node3.log  2>&1 &