import { BrowserRouter, Route, Routes } from "react-router-dom"
import UserPage from "./components/UserPage"
import Auth from "./components/Login"
import Registration from "./components/Registration"
import CreateDict from "./components/CreateDict"
const Router = () => {
    return <BrowserRouter>
        <Routes>
            <Route element={<UserPage/>} path = '/' />
            <Route element={<UserPage/>} path = '/profile' />
            <Route element={<Auth/>} path = '/login' />
            <Route element={<Registration/>} path = '/register' />
            <Route element={<CreateDict/>} path = '/dict/create' />
            <Route path = '*' element = {<div>Страница не найдена</div>} />
        </Routes>
    </BrowserRouter>
}

export default Router