import { BrowserRouter, Route, Routes } from "react-router-dom"
import UserPage from "./components/UserPage"
import Auth from "./components/Login"
import Registration from "./components/Registration"
import CreateDict from "./components/CreateDict"
import SharedDict from "./components/SharedDict"
import Quizlet from "./components/Quizlet"
const Router = () => {
    return <BrowserRouter>
        <Routes>
            <Route element={<UserPage/>} path = '/' />
            <Route element={<UserPage/>} path = '/profile' />
            <Route element={<Auth/>} path = '/login' />
            <Route element={<Registration/>} path = '/register' />
            <Route element={<CreateDict/>} path = '/dict/create' />
            <Route element={<SharedDict/>} path = '/dict/shared/:id' />
            <Route element={<Quizlet/>} path = '/quizlet' />
            <Route path = '*' element = {<div>Страница не найдена</div>} />
        </Routes>
    </BrowserRouter>
}

export default Router