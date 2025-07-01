import { TaskMaster } from "@/components/task-master";

export default function Home() {
  return (
    <main className="flex min-h-screen w-full flex-col items-center justify-start p-4 pt-8 sm:pt-12 md:pt-24">
      <TaskMaster />
    </main>
  );
}
