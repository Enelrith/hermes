PORT=8080
ENDPOINT="/stripe-webhook"


echo "Starting stripe webhook at: http://localhost:$PORT$ENDPOINT"

stripe listen --forward-to http://localhost:$PORT$ENDPOINT