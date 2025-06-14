// Generated by Copilot
import { defineConfig, devices } from '@playwright/test';

export default defineConfig({
  testDir: './e2e',
  fullyParallel: false,
  forbidOnly: !!process.env.CI,
  retries: 0,
  workers: 1,
  reporter: 'html',
  use: {
    // No baseURL needed as tests specify full URLs
    trace: 'on',
    screenshot: 'on',
    video: 'on',
    headless: false, // Make browser visible during test execution
  },

  projects: [
    {
      name: 'chromium',
      use: { ...devices['Desktop Chrome'] },
    },
  ],
});
