import { render, screen } from '@testing-library/react';
import App from './App';

test('renders RH management system', () => {
  render(<App />);
  const titleElement = screen.getByText(/Système de gestion RH/i);
  expect(titleElement).toBeInTheDocument();
});
