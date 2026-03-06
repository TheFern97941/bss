import {defineRouting} from "next-intl/routing";
import {hasLocale} from "use-intl";
import type {Locale} from "use-intl/core";
import {permanentRedirect} from "next/navigation";


export const LANGUAGES = [
  { code: "zh", name: "简体中文", flag: "🇨🇳" },
  { code: "en", name: "English", flag: "🇺🇸" },
];

const locales = LANGUAGES.map(lang => lang.code)

export const routing = defineRouting({
  locales: locales,
  defaultLocale: "zh",
  localePrefix: "always",
  localeDetection: true,
})


export const checkAndGetLocal = (locale: string): Locale => {
  if (!hasLocale(routing.locales, locale)) {
    console.log("checkAndGetLocal hasLocale : ", locale);
    permanentRedirect(`/${routing.defaultLocale}`);
  }

  return locale as Locale;
}