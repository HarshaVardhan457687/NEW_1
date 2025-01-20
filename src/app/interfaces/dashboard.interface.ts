export interface Statistics {
  projects: MetricData;
  objectives: MetricData;
  tasks: MetricData;
}

export interface MetricData {
  active: number;
  total: number;
}

export interface Task {
  id: string;
  title: string;
  dueDate: Date;
  type: TaskType;
  project: string;
  priority: Priority;
}

export enum TaskType {
  Documentation = 'Documentation',
  Planning = 'Planning'
}

export enum Priority {
  High = 'High Priority',
  Medium = 'Medium Priority',
  Low = 'Low Priority'
} 