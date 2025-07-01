'use client';

import { useState, useEffect } from 'react';
import type { Task } from '@/types';
import { AddTaskForm } from '@/components/add-task-form';
import { TaskList } from '@/components/task-list';
import { AiSuggestions } from '@/components/ai-suggestions';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Separator } from '@/components/ui/separator';

const API_URL = 'http://localhost:8080/api/tasks';

export function TaskMaster() {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch(API_URL)
      .then((res) => res.json())
      .then((data) => setTasks(data))
      .catch((err) => console.error('Failed to fetch tasks', err))
      .finally(() => setLoading(false));
  }, []);

  const handleAddTask = async (text: string) => {
    try {
      const res = await fetch(API_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ title: text })
      });
      if (res.ok) {
        const newTask = await res.json();
        setTasks((prev) => [...prev, { id: newTask.id, text: newTask.title, completed: newTask.done }]);
      }
    } catch (err) {
      console.error('Failed to add task', err);
    }
  };

  const handleToggleComplete = async (id: number) => {
    try {
      const res = await fetch(`${API_URL}/${id}/done`, { method: 'PATCH' });
      if (res.ok) {
        setTasks((prev) => prev.map((task) => task.id === id ? { ...task, completed: !task.completed } : task));
      }
    } catch (err) {
      console.error('Failed to mark task as done', err);
    }
  };

  const handleUpdateTask = async (id: number, newText: string) => {
    try {
      const res = await fetch(`${API_URL}/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ title: newText })
      });
      if (res.ok) {
        setTasks((prev) => prev.map((task) => task.id === id ? { ...task, text: newText } : task));
      }
    } catch (err) {
      console.error('Failed to update task', err);
    }
  };

  const handleDeleteTask = async (id: number) => {
    try {
      const res = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
      if (res.ok) {
        setTasks((prev) => prev.filter((task) => task.id !== id));
      }
    } catch (err) {
      console.error('Failed to delete task', err);
    }
  };

  if (loading) {
    return (
      <Card className="w-full max-w-2xl shadow-lg">
        <CardHeader>
          <CardTitle className="text-3xl font-bold tracking-tight">TaskMaster</CardTitle>
          <CardDescription>Loading tasks...</CardDescription>
        </CardHeader>
      </Card>
    );
  }

  return (
    <Card className="w-full max-w-2xl shadow-lg animate-in fade-in duration-500">
      <CardHeader>
        <CardTitle className="text-3xl font-bold tracking-tight">TaskMaster</CardTitle>
        <CardDescription>Your intelligent to-do list to keep you organized.</CardDescription>
      </CardHeader>
      <CardContent className="flex flex-col gap-6">
        <AddTaskForm onAddTask={handleAddTask} />
        <TaskList
          tasks={tasks}
          onToggleComplete={handleToggleComplete}
          onUpdateTask={handleUpdateTask}
          onDeleteTask={handleDeleteTask}
        />
        <Separator />
        <AiSuggestions tasks={tasks} onAddTask={handleAddTask} />
      </CardContent>
    </Card>
  );
}
