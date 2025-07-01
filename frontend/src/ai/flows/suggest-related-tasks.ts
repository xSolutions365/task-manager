'use server';

/**
 * @fileOverview AI agent that suggests related tasks based on the existing todo list.
 *
 * - suggestRelatedTasks - A function that suggests related tasks.
 * - SuggestRelatedTasksInput - The input type for the suggestRelatedTasks function.
 * - SuggestRelatedTasksOutput - The return type for the suggestRelatedTasks function.
 */

import {ai} from '@/ai/genkit';
import {z} from 'genkit';

const SuggestRelatedTasksInputSchema = z.object({
  tasks: z.array(z.string()).describe('The list of existing tasks.'),
});
export type SuggestRelatedTasksInput = z.infer<typeof SuggestRelatedTasksInputSchema>;

const SuggestRelatedTasksOutputSchema = z.object({
  suggestions: z.array(z.string()).describe('Suggestions for related tasks.'),
});
export type SuggestRelatedTasksOutput = z.infer<typeof SuggestRelatedTasksOutputSchema>;

export async function suggestRelatedTasks(input: SuggestRelatedTasksInput): Promise<SuggestRelatedTasksOutput> {
  return suggestRelatedTasksFlow(input);
}

const suggestRelatedTasksPrompt = ai.definePrompt({
  name: 'suggestRelatedTasksPrompt',
  input: {schema: SuggestRelatedTasksInputSchema},
  output: {schema: SuggestRelatedTasksOutputSchema},
  prompt: `You are a helpful task management assistant. Given the following list of tasks, suggest other related tasks that the user might want to add to their todo list. Be concise.

Tasks:
{{#each tasks}}- {{{this}}}
{{/each}}`,
});

const suggestRelatedTasksFlow = ai.defineFlow(
  {
    name: 'suggestRelatedTasksFlow',
    inputSchema: SuggestRelatedTasksInputSchema,
    outputSchema: SuggestRelatedTasksOutputSchema,
  },
  async input => {
    const {output} = await suggestRelatedTasksPrompt(input);
    return output!;
  }
);
