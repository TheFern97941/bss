import { routing } from '@/i18n/routing';
import { formats } from '@/i18n/request';
import messages from './messages/zh.json';

declare module 'next-intl' {
  interface AppConfig {
    Locale: (typeof routing.locales)[number];
    Messages: typeof messages;
    Formats: typeof formats;
  }
}

declare global {
  type I18nParamsType = Promise<{locale: string}>
}
