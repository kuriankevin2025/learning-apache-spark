import os
import time
import random
from datetime import datetime, timedelta

LOG_FILENAME = 'app_data.log'
START_TIME = datetime(2023, 1, 1, 0, 0, 0)

# Modes: 'SIZE' or 'LINES'
MODE = 'SIZE'

BATCH_SIZE = 100
LOG_SIZE_MB = 100

LOG_LINE = 100

RAND_INT_START = 1
RAND_INT_END = 500
DATA_TIME_FORMAT = '%a %b %d %H:%M:%S %Y'

log_levels = ['TRACE', 'DEBUG', 'INFO', 'WARN', 'ERROR', 'FATAL']
counts = {level: 0 for level in log_levels}

def generate_log_file_by_size():
    log_datetime = START_TIME
    log_size_bytes = LOG_SIZE_MB * 1024 * 1024
    with open(LOG_FILENAME, 'w') as f:
        while True:
            batch_entries = []
            for _ in range(BATCH_SIZE):
                level = random.choice(log_levels)
                counts[level] += 1
                delta = random.randint(RAND_INT_START, RAND_INT_END)
                log_datetime += timedelta(seconds=delta)
                timestamp = log_datetime.strftime(DATA_TIME_FORMAT)
                batch_entries.append(f'{level}: {timestamp}\n')
            f.writelines(batch_entries)
            f.flush()
            if f.tell() >= log_size_bytes:
                break
    end_time = log_datetime
    return START_TIME, end_time, counts

def generate_log_file_by_lines():
    log_datetime = START_TIME
    with open(LOG_FILENAME, 'w') as f:
        for _ in range(LOG_LINE):
            level = random.choice(log_levels)
            counts[level] += 1
            delta = random.randint(RAND_INT_START, RAND_INT_END)
            log_datetime += timedelta(seconds=delta)
            timestamp = log_datetime.strftime(DATA_TIME_FORMAT)
            f.write(f'{level}: {timestamp}\n')
    end_time = log_datetime
    return START_TIME, end_time, counts

def round_time(end_time):
    if end_time.minute == 0 and end_time.second == 0:
        rounded_end_time = end_time
    else:
        rounded_end_time = end_time.replace(minute=0, second=0, microsecond=0) + timedelta(hours=1)
    return rounded_end_time

def main():
    start_time = time.time()
    if MODE == 'SIZE':
        start_datetime, end_datetime, counts = generate_log_file_by_size()
    elif MODE == 'LINES':
        start_datetime, end_datetime, counts = generate_log_file_by_lines()
    end_time = time.time()
    
    file_path = os.path.join(os.getcwd(), LOG_FILENAME)
    file_size = os.path.getsize(file_path) / (1024 * 1024)

    print("Log Generation Script Execution Summary:")
    print(f"Log Path: {file_path}")
    print(f"Execution Time: {end_time - start_time} seconds")
    
    if MODE == 'SIZE':
        print(f"Target File Size: {LOG_SIZE_MB} MB")
        print(f"Actual File Size: {file_size:.2f} MB")
    elif MODE == 'LINES':
        print(f"Number of Entries: {LOG_LINE}")
    
    print(f"Start Date-Time: {start_datetime.strftime(DATA_TIME_FORMAT)}")
    print(f"End Date-Time:   {round_time(end_datetime).strftime(DATA_TIME_FORMAT)}")
    
    print("Log Levels Count:")
    for level in counts:
        print(f"  {level}: {counts[level]}")

if __name__ == "__main__":
    main()