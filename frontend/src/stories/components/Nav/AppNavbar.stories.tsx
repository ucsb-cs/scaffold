import type { Meta, StoryObj } from "@storybook/react";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { MemoryRouter } from "react-router-dom";
import AppNavbar from "main/components/Nav/AppNavbar";
import { currentUserFixtures } from "fixtures/currentUserFixtures";
import { systemInfoFixtures } from "fixtures/systemInfoFixtures";

function withProviders(currentUser: unknown, systemInfo: unknown) {
  return (Story: () => React.JSX.Element) => {
    const qc = new QueryClient({
      defaultOptions: { queries: { retry: false, staleTime: Infinity } },
    });
    qc.setQueryData(["current user"], currentUser);
    qc.setQueryData(["systemInfo"], systemInfo);
    return (
      <QueryClientProvider client={qc}>
        <MemoryRouter>
          <Story />
        </MemoryRouter>
      </QueryClientProvider>
    );
  };
}

const meta: Meta<typeof AppNavbar> = {
  title: "components/Nav/AppNavbar",
  component: AppNavbar,
  parameters: {
    layout: "fullscreen",
  },
  tags: ["autodocs"],
};

export default meta;
type Story = StoryObj<typeof AppNavbar>;

export const LoggedOut: Story = {
  decorators: [
    withProviders(
      currentUserFixtures.notLoggedIn,
      systemInfoFixtures.showingNeither,
    ),
  ],
};

export const LoggedIn: Story = {
  decorators: [
    withProviders(
      currentUserFixtures.userOnly,
      systemInfoFixtures.showingNeither,
    ),
  ],
};

export const AdminUser: Story = {
  decorators: [
    withProviders(
      currentUserFixtures.adminUser,
      systemInfoFixtures.showingNeither,
    ),
  ],
};
