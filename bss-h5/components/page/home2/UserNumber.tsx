"use client"
import {useEffect, useRef, useState } from "react";
import {useFormatter} from "next-intl";

function easeOutQuad(t: number) {
  return t * (2 - t)
}


function UserNumber({number}: {number: number}) {

  const [userNumber, setUserNumber] = useState(number)
  const timerRef = useRef<ReturnType<typeof setInterval>>(undefined)
  const formatter = useFormatter()
  // 每5秒添加一次数量
  const addCounterStepMillis = 5000
  // 1.5秒执行完添加动画
  const totalDuration = 1500

  useEffect(() => {

    timerRef.current = setInterval(() => {
      // 这个做成随机数
      const addNumber = 32

      for (let i = 0; i < addNumber; i++) {
        const progress = i / (addNumber - 1) // 0 到 1
        const eased = easeOutQuad(progress)
        const delay = eased * totalDuration

        setTimeout(() => {
          setUserNumber(prev => prev + 1)
        }, delay)
      }

    }, addCounterStepMillis)

    return () => clearInterval(timerRef.current)
  }, [])

  return (
    <div className={"inline-block mr-10"}>
      <h1 className="text-primary scroll-m-20 text-center text-7xl font-extrabold tracking-tight text-balance primary">
        {
          formatter.number(userNumber)
        }
      </h1>
    </div>
  )
}

export default UserNumber;