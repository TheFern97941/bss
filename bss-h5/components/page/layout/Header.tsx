"use client";

import { useState } from "react";
import {
  NavigationMenu,
  NavigationMenuItem,
  NavigationMenuList,
  navigationMenuTriggerStyle
} from "@/components/ui/navigation-menu";
import { Link } from "@/i18n/navigation";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { 
  Menu,
  X
} from "lucide-react";
import {ModeToggle} from "@/components/mode-toggle";
import {LangToggle} from "@/components/lang-toggle";
import {SearchButton} from "@/components/search-button";
import {Logo} from "@/components/page/home/Logo";

interface HeaderItem {
  title: string;
  href: string;
  icon?: string;
  desc?: string;
}

const HEADER_ITEMS: HeaderItem[] = [
  {
    title: "行情",
    href: "/markets",
  },
  {
    title: "交易",
    href: "/trade",
  },
  {
    title: "合约",
    href: "/futures",
  },
  {
    title: "广场",
    href: "/square",
  }
]

const Header = () => {

  return (
    <header className="sticky top-0 z-50 w-full border-b bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
      <div className="mx-auto px-16">
        {/* pc端菜单 */}
        <div className="flex h-16 items-center justify-between">
          <HeaderLeft />
          <HeaderRight />
        </div>
      </div>
    </header>
  );
}

// 左侧导航组件
const HeaderLeft = () => {
  return (
    <div className="flex items-center gap-4">
      <Logo />
      {/* 主导航 - 桌面端 */}
      <NavigationMenu className="hidden lg:flex" viewport={false}>
        <NavigationMenuList>
          {HEADER_ITEMS.map((item) => (
            <NavigationMenuItem key={item.title}>
              <Link 
                href={item.href}
                className={navigationMenuTriggerStyle()}
              >
                {item.title}
              </Link>
            </NavigationMenuItem>
          ))}
        </NavigationMenuList>
      </NavigationMenu>
    </div>
  );
};

// 右侧功能区组件
const HeaderRight = () => {
  return (
    <div className="flex items-center gap-2">
      <SearchButton />
      
      {/* 登录/注册 */}
      <div className="hidden lg:flex items-center gap-2">
        <Button variant="ghost" size="sm">
          登录
        </Button>
        <Button size="sm">
          注册
        </Button>
      </div>

      {/* 语言切换 */}
      <LangToggle />

      {/* 主题切换 */}
      <ModeToggle />
    </div>
  );
};



export default Header