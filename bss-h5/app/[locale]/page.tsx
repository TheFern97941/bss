"use client";

export default function Page() {

  return (
    <div className="relative m-auto max-w-1/3 pt-10">
      <div className="columns-2">
        <p>Well, let me tell you something, ...</p>
        <p>Sure, go ahead, laugh...</p>
        <p>Maybe we can live without...</p>
        <p>Look. If you think this is...</p>
      </div>
      <div>
        <span className="box-decoration-slice bg-linear-to-r from-indigo-600 to-pink-500 px-2 text-white ...">
          Hello<br/>World
        </span>
      </div>
      <div>
        <span className="box-decoration-clone bg-linear-to-r from-indigo-600 to-pink-500 px-2 text-white ...">
          Hello<br/>World
        </span>
      </div>
      <div>
        <div className="relative w-52 h-52 m-5 bg-blue-300 isolation-auto">
          <div className="absolute w-24 h-24 bg-green-500 z-10"></div>
        </div>

        <div className="relative w-52 h-52 m-5 bg-blue-300 isolation-isolate">
          <div className="absolute w-24 h-24 bg-green-500 z-10"></div>
        </div>

        <div className="absolute top-10 left-10 w-36 h-36 bg-red-500 z-20"></div>
      </div>
    </div>
  );
}
