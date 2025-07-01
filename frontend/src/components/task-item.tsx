'use client';

import { useState, useRef, useEffect } from 'react';
import type { Task } from '@/types';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Checkbox } from '@/components/ui/checkbox';
import { Pencil, Trash2, Save, X } from 'lucide-react';
import { cn } from '@/lib/utils';

interface TaskItemProps {
  task: Task;
  onToggleComplete: (id: number) => void;
  onUpdateTask: (id: number, newText: string) => void;
  onDeleteTask: (id: number) => void;
}

export function TaskItem({ task, onToggleComplete, onUpdateTask, onDeleteTask }: TaskItemProps) {
  const [isEditing, setIsEditing] = useState(false);
  const [editText, setEditText] = useState(task.text);
  const inputRef = useRef<HTMLInputElement>(null);

  // Sync editText with task.text when task changes
  useEffect(() => {
    setEditText(task.text);
  }, [task.text]);

  useEffect(() => {
    if (isEditing) {
      inputRef.current?.focus();
    }
  }, [isEditing]);

  const handleCancelEdit = () => {
    setEditText(task.text);
    setIsEditing(false);
  };

  const handleUpdate = () => {
    const trimmedText = editText.trim();
    if (trimmedText) {
      onUpdateTask(task.id, trimmedText);
      setIsEditing(false);
    } else {
      // If the input is empty, cancel the edit and revert to original text.
      handleCancelEdit();
    }
  };
  
  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      handleUpdate();
    } else if (e.key === 'Escape') {
      handleCancelEdit();
    }
  }

  return (
    <li className="group flex items-center gap-3 rounded-md p-2 transition-colors hover:bg-secondary/50">
      <Checkbox
        id={`task-${task.id}`}
        checked={task.completed}
        onCheckedChange={() => onToggleComplete(task.id)}
        aria-label={`Mark ${task.text} as ${task.completed ? 'incomplete' : 'complete'}`}
      />
      {isEditing ? (
        <Input
          ref={inputRef}
          value={editText}
          onChange={(e) => setEditText(e.target.value)}
          onKeyDown={handleKeyDown}
          onBlur={handleUpdate} // Auto-saves when the input loses focus
          className="flex-1 h-9"
          aria-label="Edit task text"
        />
      ) : (
        <label
          htmlFor={`task-${task.id}`}
          className={cn(
            'flex-1 cursor-pointer text-sm font-medium',
            task.completed && 'text-muted-foreground line-through'
          )}
        >
          {task.text}
        </label>
      )}

      <div className="flex items-center gap-1">
        {isEditing ? (
          <>
            <Button variant="ghost" size="icon" onMouseDown={(e) => { e.preventDefault(); handleUpdate(); }} aria-label="Save task">
              <Save className="h-4 w-4 text-green-600" />
            </Button>
            <Button variant="ghost" size="icon" onMouseDown={(e) => { e.preventDefault(); handleCancelEdit(); }} aria-label="Cancel edit">
              <X className="h-4 w-4" />
            </Button>
          </>
        ) : (
          <>
            <Button
              variant="ghost"
              size="icon"
              onClick={() => setIsEditing(true)}
              className="opacity-0 group-hover:opacity-100 focus:opacity-100 transition-opacity"
              aria-label="Edit task"
            >
              <Pencil className="h-4 w-4 text-primary" />
            </Button>
            <Button
              variant="ghost"
              size="icon"
              onClick={() => onDeleteTask(task.id)}
              className="opacity-0 group-hover:opacity-100 focus:opacity-100 transition-opacity"
              aria-label="Delete task"
            >
              <Trash2 className="h-4 w-4 text-destructive" />
            </Button>
          </>
        )}
      </div>
    </li>
  );
}
