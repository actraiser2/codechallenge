import { Route, Routes } from 'react-router-dom'
import AccountListComponent from './components/AccountListComponent'
import AccountComponent from './components/AccountComponent'


function App() {
  return (
    <Routes>
      <Route path="/accountDetail/:accountId" element={<AccountComponent/>} />
      <Route path="/" element={<AccountListComponent/>} />
    </Routes>
  );
}

export default App;
