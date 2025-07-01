'use server';

import { suggestRelatedTasks } from '@/ai/flows/suggest-related-tasks';

export async function getAiSuggestions(tasks: string[]) {
  if (tasks.length === 0) {
    return { suggestions: [] };
  }
  
  try {
    const result = await suggestRelatedTasks({ tasks });
    return { suggestions: result.suggestions };
  } catch (error) {
    console.error('Error fetching AI suggestions:', error);
    return { error: 'Failed to get AI suggestions. Please try again later.' };
  }
}
