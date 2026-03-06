import {QRCodeSVG} from "qrcode.react";
import OpenThirdLink from "@/components/page/home/OpenThirdLink";


function ContactSection() {
  return (
    <div className="section" key={"contact-section"}>
      <div className="text-center text-5xl font-extrabold tracking-tight text-balance primary mb-12">联系我们</div>
      <div className="flex justify-center space-x-24">
        {
          [
            {url: "/contact/telegram.png", name: "Telegram", href: ""},
            {url: "/contact/proton.svg", name: "Proton Mail", href: ""},
            {url: "/contact/libera.svg", name: "Libear chat", href: ""},
            {url: "/contact/github.png", name: "Github", href: ""},
            {url: "/contact/signal.png", name: "Signal chat", href: ""},
          ]
            .map((item, index) => (
              <div key={index} className="flex flex-col items-center gap-4">
                <div className="flex space-x-4 justify-center items-center">
                  <span className={"mt-1 font-medium text-sm"}>{item.name}</span>
                </div>
                <QRCodeSVG
                  value={item.url}
                  bgColor="#000000"
                  fgColor="#FFFFFF"
                  imageSettings={{
                    src: item.url,
                    height: 28,
                    width: 28,
                    excavate: true,
                  }}
                />
                <OpenThirdLink tLink={item.url} />
              </div>
            ))
        }
      </div>
    </div>
  )
}

export default ContactSection;
