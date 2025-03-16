# CPU-Scheduling-Simulator

A Java implementation of common CPU scheduling algorithms with process statistics and Gantt chart visualization.

## Features

- Implements 4 scheduling algorithms:
  - First-Come, First-Served (FCFS)
  - Non-preemptive Shortest Job First (SJF)
  - Preemptive Shortest Remaining Time (SRT)
  - Round Robin (RR)
- Interactive menu-driven interface
- Process statistics calculation:
  - Completion Time
  - Waiting Time
  - Turnaround Time
  - Average waiting and turnaround times
- Gantt chart visualization
- Input validation and error handling

## Algorithms Implemented

### 1. First-Come, First-Served (FCFS)
- Non-preemptive
- Processes executed in order of arrival
- Simple but may result in long waiting times

### 2. Shortest Job First (SJF)
- Non-preemptive
- Selects process with smallest burst time from ready queue
- Can minimize average waiting time

### 3. Shortest Remaining Time (SRT)
- Preemptive version of SJF
- Always executes process with shortest remaining time
- Requires tracking of remaining burst time

### 4. Round Robin (RR)
- Preemptive with time quantum
- Fair allocation of CPU time
- Prevents starvation of processes

## Input Requirements
- Number of processes (must be > 0)
- For each process:
  - Process ID (string)
  - Arrival time (non-negative integer)
  - Burst time (positive integer)
- Time quantum (for Round Robin only)

## Output Details
For each algorithm, the program displays:
- Process execution statistics in tabular format:
