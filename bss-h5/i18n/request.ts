import {getRequestConfig} from "next-intl/server";
import {Formats, hasLocale} from "use-intl";
import {routing} from "@/i18n/routing";

export default getRequestConfig(async ({requestLocale}) => {
  const requested = await requestLocale;
  const locale =  hasLocale(routing.locales, requested) ? requested : routing.defaultLocale

  return {
    locale,
    messages: (await import(`../messages/${locale}.json`)).default,
  }
})

export const formats = {
  dateTime: {
    short: {
      day: 'numeric',
      month: 'numeric',
      year: 'numeric'
    }
  },
  number: {
    precise: {
      maximumFractionDigits: 6
    }
  },
  list: {
    enumeration: {
      style: 'long',
      type: 'conjunction'
    }
  }
} satisfies Formats;

