"use client";

import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Search, X } from "lucide-react";

export function SearchButton() {
  const [showSearch, setShowSearch] = useState(false);

  return (
    <div className="hidden lg:flex items-center">
      {showSearch ? (
        <div className="flex items-center gap-2">
          <Input
            type="search"
            placeholder="搜索币种..."
            className="w-64"
            autoFocus
            onBlur={() => setTimeout(() => setShowSearch(false), 200)}
          />
          <Button
            variant="ghost"
            size="icon"
            onClick={() => setShowSearch(false)}
          >
            <X className="size-5" />
          </Button>
        </div>
      ) : (
        <Button
          variant="ghost"
          size="icon"
          onClick={() => setShowSearch(true)}
        >
          <Search className="size-5" />
        </Button>
      )}
    </div>
  );
}