#/bin/bash
tip=homework

kitty --hold make server TYPE=$tip &
sleep 2
kitty --hold make client TYPE=$tip &
kitty --hold make client TYPE=$tip &
# kitty --hold make client TYPE=$tip &
