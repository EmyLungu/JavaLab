#/bin/bash
tip=homework

NUM_BOTS=50

cleanup() {
    echo "Shutting down bots..."
    kill 0
}

# Trap SIGINT (Ctrl+C) and SIGTERM
trap cleanup EXIT


kitty --hold sh -c "make server TYPE=$tip 2>&1 | tee server_log.txt" &
sleep 2

echo "Spawning $NUM_BOTS bots..."
for i in $(seq 1 $NUM_BOTS); do
    make random-bot > /dev/null & 
done

wait
