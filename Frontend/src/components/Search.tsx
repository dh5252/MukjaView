import { IoSearch } from "react-icons/io5";

export default function Search(){

    let searchbar = "w-full grid grid-cols-[25px_65px_auto_40px] items-center gap-[5px]"

    return(
        <div className={searchbar} >
            <IoSearch size={"25"}/>
            <select className="focus:outline-none text-center">
                <option>가게명</option>
                <option>태그</option>
            </select>
            <input className="focus:outline-none w-[100px]" placeholder="검색하기.."/>
            <button className="w-10">찾기</button>
        </div>
    )
}