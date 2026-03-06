import {Geist, Geist_Mono} from "next/font/google";
import "../globals.css";
import {ThemeProvider} from "@/components/theme-provider";
import {NextIntlClientProvider} from "next-intl";
import {checkAndGetLocal} from "@/i18n/routing";
import {getTranslations, setRequestLocale} from "next-intl/server";
import {getLangDir} from "rtl-detect";
import {Metadata} from "next";
import Header from "@/components/page/layout/Header";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});



export async function generateMetadata({ params }: { params: I18nParamsType}): Promise<Metadata> {
  const {locale} = await params;

  const t = await getTranslations({locale: checkAndGetLocal(locale), namespace: "RootLayout"})
  return {
    title: t("title"),
    description: t("description"),
  }
}

interface RootLayoutProps {
  children: React.ReactNode;
  params: I18nParamsType
}

export default async function RootLayout({children, params}: Readonly<RootLayoutProps>) {

  const {locale} = await params;
  const direction = getLangDir(locale);
  const dLocale = checkAndGetLocal(locale)

  setRequestLocale(dLocale);

  return (
    <html lang={locale} dir={direction} suppressHydrationWarning>
    <body
      className={`${geistSans.variable} ${geistMono.variable} antialiased min-h-screen`}
    >
    <NextIntlClientProvider>
      <ThemeProvider
        attribute="class"
        defaultTheme="dark"
        enableSystem
        disableTransitionOnChange
      >
        <Header />
        {children}
      </ThemeProvider>
    </NextIntlClientProvider>
    </body>
    </html>
  );
}
