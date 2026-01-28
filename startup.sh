# Continuous Integration pull, test, package - Goal: Merge + catch errors early
echo "Building the project..."
mvn clean package
if [ $? -ne 0 ]; then
    echo "Build failed."
    exit 1
fi

# CD (Continuous Deployment) - Goal: Automatically deploy validated code to production.
echo "Stopping and removing existing container..."
docker stop starter 2>/dev/null
# 2 is stderr
if [ $? -ne 0 ]; then echo "Container not running."; fi
# $? is exit status of the previous command

docker rm starter 2>/dev/null
if [ $? -ne 0 ]; then echo "Container does not exist."; fi

echo "Building Docker image..."
docker build -t starter-app .

echo "Running Docker container..."
docker run -d --name starter -p 8080:8080 -e DB_URL="jdbc:mysql://host.docker.internal:3306/hermes" starter-app

echo "Waiting for springboot to boot up..."
while true; do
  # capture the output of a command and use it like a variable
    STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080)
if [ "$STATUS" != "000" ]; then
    echo -e "\n Server responded with status: $STATUS"
    break
fi
    echo -n "."
    sleep 1
done

echo " Server is online."
