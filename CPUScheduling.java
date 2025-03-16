import java.util.*;
import java.util.InputMismatchException;

// Class representing a process in the scheduling system
class Process {
    String id;                // Process identifier
    int arrivalTime;          // Time when process arrives in ready queue
    int burstTime;            // CPU time required by the process
    int remainingTime;        // Remaining CPU time needed (for preemptive algorithms)
    int completionTime;       // Time when process completes execution
    int waitingTime;          // Total time spent waiting in ready queue
    int turnaroundTime;       // Total time from arrival to completion (waiting + burst)

    Process(String id, int arrivalTime, int burstTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;  // Initialize remaining time to full burst time
    }
}

public class CPUScheduling {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            // Display main menu
            System.out.println("\nCPU Scheduling Algorithms");
            System.out.println("===========================");
            System.out.println("1. First-Come, First-Served (FCFS)");
            System.out.println("2. Shortest-Job-First (SJF)");
            System.out.println("3. Shortest-Remaining-Time (SRT)");
            System.out.println("4. Round Robin (RR)");
            System.out.println("5. Exit\n");
            
            int choice = 0;
            // Input validation loop for menu selection
            while(true){
                try {
                    System.out.print("Select an option: ");
                    choice = scanner.nextInt();
                    scanner.nextLine();
                    if(choice < 1 || choice > 5) {
                        System.out.println("Wrong input!!!");
                        continue;
                    }        
                } catch (Exception e) {
                    System.out.println("Please enter the Integer.");
                    scanner.nextLine();
                    continue;
                }
                break;
            }

            // Exit condition
            if (choice == 5) {
                System.out.println("Thank You :)");
                break;
            }

            // Get process list from user input
            List<Process> processes = getProcesses();
            
            // Execute selected algorithm
            switch (choice) {
                case 1: fcfs(processes); break;
                case 2: sjf(processes); break;
                case 3: srt(processes); break;
                case 4: roundRobin(processes); break;
                default: System.out.println("Invalid choice. Try again.\n");
            }
        }
    }
        
    // Method to collect process information from user
    private static List<Process> getProcesses() {
        int n = 0;
        int arrivalTime = 0;
        int burstTime = 0;

        // Input validation for number of processes
        while (true) {
            try {
                System.out.print("Enter number of processes: ");
                n = scanner.nextInt();
                scanner.nextLine();
                if (n <= 0) {
                    System.out.println("Number of process must be more than 0.");
                    continue;
                }
            } catch (Exception e) {
                System.out.println("Please enter integer numbers.");
                scanner.nextLine();
                continue;
            }
            break;
        }
        
        List<Process> processes = new ArrayList<>();
        
        // Collect details for each process
        for (int i = 1; i <= n; i++) {
            System.out.print("Enter Process ID: ");
            String id = scanner.next();
            
            // Input validation for arrival time
            while (true) {                
                try{
                    System.out.print("Enter Arrival Time: ");
                    arrivalTime = scanner.nextInt();
                    scanner.nextLine();
                    if (arrivalTime < 0) {
                        System.out.println("Arrival time must be positive integer.");
                        continue;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Please enter integer numbers.");
                    scanner.nextLine();
                    continue;
                }
                break;
            }

            // Input validation for burst time
            while (true) {
                try {
                    System.out.print("Enter Burst Time: ");
                    burstTime = scanner.nextInt();
                    scanner.nextLine();
                    if (burstTime <= 0) {
                        System.out.println("Burst Time must be more than 0.");
                        continue;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Please enter the integer numbers.");
                    scanner.nextLine();
                    continue;
                }
                break;
            }

            processes.add(new Process(id, arrivalTime, burstTime));
        }
        return processes;
    }

    // First-Come First-Served scheduling algorithm
    private static void fcfs(List<Process> processes) {
        // Sort processes by arrival time
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        int currentTime = 0;
        
        for (Process p : processes) {
            // Handle idle time between processes
            currentTime = Math.max(currentTime, p.arrivalTime);
            // Calculate completion time
            p.completionTime = currentTime + p.burstTime;
            // Calculate turnaround time (CT - AT)
            p.turnaroundTime = p.completionTime - p.arrivalTime;
            // Calculate waiting time (TAT - BT)
            p.waitingTime = p.turnaroundTime - p.burstTime;
            currentTime += p.burstTime;  // Move to next available time slot
        }
        printResults(processes);
    }

    // Non-preemptive Shortest Job First scheduling
    private static void sjf(List<Process> processes) {
        List<Process> readyQueue = new ArrayList<>();
        List<Process> sortedProcesses = new ArrayList<>(processes);
        // Sort by arrival time first
        sortedProcesses.sort(Comparator.comparingInt(p -> p.arrivalTime));
        int currentTime = 0;
        
        while (!sortedProcesses.isEmpty() || !readyQueue.isEmpty()) {
            // Add arrived processes to ready queue
            while (!sortedProcesses.isEmpty() && sortedProcesses.get(0).arrivalTime <= currentTime) {
                readyQueue.add(sortedProcesses.remove(0));
            }
            
            if (!readyQueue.isEmpty()) {
                // Sort ready queue by burst time (shortest first)
                readyQueue.sort(Comparator.comparingInt(p -> p.burstTime));
                Process p = readyQueue.remove(0);
                // Process executes completely once started (non-preemptive)
                currentTime = Math.max(currentTime, p.arrivalTime) + p.burstTime;
                p.completionTime = currentTime;
                p.turnaroundTime = p.completionTime - p.arrivalTime;
                p.waitingTime = p.turnaroundTime - p.burstTime;
            } else {
                // CPU idle if no processes available
                currentTime++;
            }
        }
        printResults(processes);
    }

    // Preemptive Shortest Remaining Time First scheduling
    private static void srt(List<Process> processes) {
        int time = 0;
        int completed = 0;
        List<Process> readyQueue = new ArrayList<>();
        
        while (completed < processes.size()) {
            // Add arriving processes to ready queue
            for (Process p : processes) {
                if (p.arrivalTime == time) {
                    readyQueue.add(p);
                }
            }
            // Sort by remaining time (shortest first)
            readyQueue.sort(Comparator.comparingInt(p -> p.remainingTime));
            
            if (!readyQueue.isEmpty()) {
                Process p = readyQueue.get(0);
                p.remainingTime--;
                
                // Check if process completed
                if (p.remainingTime == 0) {
                    p.completionTime = time + 1;  // +1 because we're at end of time unit
                    p.turnaroundTime = p.completionTime - p.arrivalTime;
                    p.waitingTime = p.turnaroundTime - p.burstTime;
                    readyQueue.remove(p);
                    completed++;
                }
            }
            time++;  // Increment time unit
        }
        printResults(processes);
    }

    // Round Robin scheduling algorithm
    private static void roundRobin(List<Process> processes) {
        System.out.print("Enter Time Quantum: ");
        int quantum = scanner.nextInt();
        // Initialize queue with all processes
        Queue<Process> queue = new LinkedList<>(processes);
        int time = 0;
        
        while (!queue.isEmpty()) {
            Process p = queue.poll();
            if (p.remainingTime > quantum) {
                // Process uses full quantum time
                time += quantum;
                p.remainingTime -= quantum;
                queue.add(p);  // Re-add to end of queue
            } else {
                // Process completes within remaining time
                time += p.remainingTime;
                p.remainingTime = 0;
                p.completionTime = time;
                p.turnaroundTime = p.completionTime - p.arrivalTime;
                p.waitingTime = p.turnaroundTime - p.burstTime;
            }
        }
        printResults(processes);
    }

    // Method to display scheduling results
    private static void printResults(List<Process> processes) {
        /* Table Legend:
         * P: Process ID
         * AT: Arrival Time
         * BT: Burst Time
         * CT: Completion Time
         * WT: Waiting Time
         * TAT: Turn Around Time
         */
        System.out.println("\nP\tAT\tBT\tCT\tWT\tTAT");
        System.out.println("===================================");   
        double totalWaiting = 0, totalTurnaround = 0;
        
        // Print details for each process
        for (Process p : processes) {
            System.out.printf("%s\t%d\t%d\t%d\t%d\t%d\n", 
                p.id, p.arrivalTime, p.burstTime, 
                p.completionTime, p.waitingTime, p.turnaroundTime);
            
            totalWaiting += p.waitingTime;
            totalTurnaround += p.turnaroundTime;
        }
        
        // Print summary statistics
        System.out.println("___________________________________");
        System.out.print("Number of process(es): " + processes.size());
        System.out.printf("\nAverage Waiting Time: %.2f", totalWaiting / processes.size());
        System.out.print(" ms");
        System.out.printf("\nAverage Turnaround Time: %.2f", totalTurnaround / processes.size());
        System.out.print(" ms");
        System.out.println("\n___________________________________");
    }
}