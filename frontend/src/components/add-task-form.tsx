'use client';

import { useState, type FormEvent } from 'react';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { Plus } from 'lucide-react';

interface AddTaskFormProps {
  onAddTask: (text: string) => void;
}

export function AddTaskForm({ onAddTask }: AddTaskFormProps) {
  const [text, setText] = useState('');

  const handleSubmit = (e: FormEvent) => {
    e.preventDefault();
    if (text.trim()) {
      onAddTask(text.trim());
      setText('');
    }
  };

  return (
    <form onSubmit={handleSubmit} className="flex w-full items-center space-x-2">
      <Input
        type="text"
        placeholder="Add a new task..."
        value={text}
        onChange={(e) => setText(e.target.value)}
        className="flex-1"
        aria-label="New task input"
      />
      <Button type="submit" aria-label="Add task">
        <Plus className="h-4 w-4" />
        <span className="hidden sm:inline sm:ml-2">Add</span>
      </Button>
    </form>
  );
}
