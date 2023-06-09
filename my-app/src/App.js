import logo from './logo.svg';
import './App.css';
import { Home } from './pages/home/Home';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import {Layout} from "./hoc/layout/Layout";
import { Zamówienie } from './pages/zamówienie/Zamówienie';
import { Koszyk } from './pages/koszyk/Koszyk';
import { Logowanie } from './pages/logowanie/Logowanie';
import { Rejestracja } from './pages/rejestracja/Rejestracja';
import { Konto } from './pages/konto/Konto';
import { Promocje } from './pages/promocje/Promocje';
import { Produkt } from './pages/produkt/Produkt';
import { Sprzedaj } from './pages/sprzedaj/Sprzedaj';
import { Ulubione } from './pages/ulubione/Ulubione';
import { HistoriaTransakcji } from './pages/historiaTransakcji/HistoriaTransakcji';
import { SearchProduct } from './pages/searchProduct/SearchProduct';
import AuthContext from './context/auth-context';
import {reducer, initialState} from "./reducer";
import { useReducer } from 'react';
import { MyProduct } from './pages/myProduct/MyProduct';
import { ForAdmin } from './pages/forAdmin/ForAdmin';



function App() {
  const [state, dispatch] = useReducer(reducer, initialState);

  return (
    <AuthContext.Provider value={{
      user: state.user,
      login: (user) => dispatch({ type: 'login', user: user}),
      logout: () => dispatch({ type: 'logout'})
  }}>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route index element={<Home />} />
          <Route path="/zamówienie" element={<Zamówienie />} />
          <Route path="/koszyk" element={<Koszyk />} />
          <Route path="/logowanie" element={<Logowanie />} />
          <Route path="/rejestracja" element={<Rejestracja />} />
          <Route path="/konto" element={<Konto />} />
          <Route path="/promocje" element={<Promocje />} />
          <Route path="/produkt/:id" element={<Produkt />} />
          <Route path="/sprzedaj" element={<Sprzedaj />} />
          <Route path="/ulubione" element={<Ulubione />} />
          <Route path="/searchProduct" element={<SearchProduct />}/>
          <Route path="/historiaTransakcji" element={<HistoriaTransakcji />} />
          <Route path="/myProduct" element={<MyProduct />} />
          <Route path='/forAdmin' element={<ForAdmin/>} />
          
        </Route>
      </Routes>
    </BrowserRouter>
  </AuthContext.Provider>
  );
}

export default App;
