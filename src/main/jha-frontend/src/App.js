import 'bootstrap/dist/css/bootstrap.min.css';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import AllJobHazards from './components/AllJobHazards';
import Header from './components/Header'
import CreateJha from './components/CreateJha'
import Tasks from './components/Tasks'

function App() {
  return (
    <BrowserRouter>
      <Header></Header>
      <Routes>
      <Route path='/' element={<AllJobHazards/>}></Route>
      <Route path='/createJha' element={<CreateJha/>}></Route>
      <Route path='/editJha/:title' element={<Tasks/>}></Route>
      </Routes>
    </BrowserRouter> 
  );
}

export default App;
