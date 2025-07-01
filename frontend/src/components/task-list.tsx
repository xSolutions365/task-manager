'use client';

import type { Task } from '@/types';
import { TaskItem } from '@/components/task-item';

interface TaskListProps {
  tasks: Task[];
  onToggleComplete: (id: number) => void;
  onUpdateTask: (id: number, newText: string) => void;
  onDeleteTask: (id: number) => void;
}

export function TaskList({ tasks, onToggleComplete, onUpdateTask, onDeleteTask }: TaskListProps) {
  if (tasks.length === 0) {
    return (
      <div className="flex flex-col items-center justify-center rounded-md border-2 border-dashed p-8 text-center text-muted-foreground">
        <p className="text-lg font-medium">No tasks yet!</p>
        <p className="text-sm">Add a task above to get started.</p>
      </div>
    );
  }

  return (
    <ul className="space-y-3">
      {tasks.map((task) => (
        <TaskItem
          key={task.id}
          task={task}
          onToggleComplete={onToggleComplete}
          onUpdateTask={onUpdateTask}
          onDeleteTask={onDeleteTask}
        />
      ))}
    </ul>
  );
}
