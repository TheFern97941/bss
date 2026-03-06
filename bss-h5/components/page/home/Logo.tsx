"use client";

import { Link } from "@/i18n/navigation";

export function Logo() {
  return (
    <Link href="/" className="flex items-center gap-2">
      <div className="flex h-8 w-8 items-center justify-center rounded bg-primary text-primary-foreground font-bold">
        B
      </div>
      <span className="text-xl font-bold">BSS Exchange</span>
    </Link>
  );
}