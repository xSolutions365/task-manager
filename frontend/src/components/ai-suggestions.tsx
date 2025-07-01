'use client';

import { useState } from 'react';
import type { Task } from '@/types';
import { getAiSuggestions } from '@/app/actions';
import { Button } from '@/components/ui/button';
import { Skeleton } from '@/components/ui/skeleton';
import { useToast } from '@/hooks/use-toast';
import { Sparkles, Lightbulb, Plus } from 'lucide-react';

interface AiSuggestionsProps {
  tasks: Task[];
  onAddTask: (text: string) => void;
}

export function AiSuggestions({ tasks, onAddTask }: AiSuggestionsProps) {
  const [suggestions, setSuggestions] = useState<string[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const { toast } = useToast();

  const handleFetchSuggestions = async () => {
    setIsLoading(true);
    setSuggestions([]);
    
    const taskTexts = tasks.map((t) => t.text);
    const result = await getAiSuggestions(taskTexts);

    if (result.error) {
      toast({
        variant: 'destructive',
        title: 'AI Error',
        description: result.error,
      });
    } else {
      setSuggestions(result.suggestions || []);
    }
    setIsLoading(false);
  };

  const handleAddSuggestion = (suggestion: string) => {
    onAddTask(suggestion);
    setSuggestions((prev) => prev.filter((s) => s !== suggestion));
  };

  return (
    <div className="space-y-4">
      <div className="flex items-center gap-2">
        <Lightbulb className="h-5 w-5 text-yellow-500" />
        <h3 className="text-lg font-semibold">Need some inspiration?</h3>
      </div>
      <p className="text-sm text-muted-foreground">
        Click the button below to get AI-powered task suggestions based on your current to-do list.
      </p>
      <Button onClick={handleFetchSuggestions} disabled={isLoading || tasks.length === 0} variant="outline" className="w-full sm:w-auto">
        <Sparkles className="mr-2 h-4 w-4" />
        {isLoading ? 'Thinking...' : 'Suggest Related Tasks'}
      </Button>

      {isLoading && (
        <div className="space-y-2 mt-4">
          <Skeleton className="h-10 w-full" />
          <Skeleton className="h-10 w-full" />
          <Skeleton className="h-10 w-5/6" />
        </div>
      )}

      {suggestions.length > 0 && !isLoading && (
        <div className="mt-4 space-y-2 animate-in fade-in duration-300">
           <h4 className="font-medium">Suggestions</h4>
          <ul className="space-y-2">
            {suggestions.map((suggestion, index) => (
              <li
                key={index}
                className="group flex items-center justify-between gap-2 rounded-md bg-secondary/30 p-2 text-sm"
              >
                <span>{suggestion}</span>
                <Button
                  size="sm"
                  variant="ghost"
                  onClick={() => handleAddSuggestion(suggestion)}
                  aria-label={`Add suggestion: ${suggestion}`}
                  className="text-accent-foreground/70 hover:text-accent-foreground"
                >
                  <Plus className="h-4 w-4 mr-1" /> Add
                </Button>
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}
