### Install
- gradlew build -x test
- docker-compose up --build -d

#### Use
- Build video: 
curl --header "Content-Type: application/json" --request POST --data '{"audio": "https://file-examples.com/wp-content/uploads/2017/11/file_example_WAV_1MG.wav","images": ["https://images.pexels.com/photos/60597/dahlia-red-blossom-bloom-60597.jpeg","https://images.pexels.com/photos/67636/rose-blue-flower-rose-blooms-67636.jpeg"]}' http://localhost:10001/buildVideo
- Download video: 
wget http://localhost:10001/video/FOOtfXXb
