import ApiClient from "../services/ApiClient";

import { useParams } from "react-router";
import { useEffect, useState } from "react";

function AccountComponent(props){
    const accountId = useParams().accountId;
    const [account, setAccount] = useState({});
    
    useEffect(() => {
        ApiClient.getAccount(accountId).then(r=> r.json()).then(data => {
            console.log("Data:" + JSON.stringify(data))
            setAccount(data)
        })
    }, []);

    return (
        <div className="container mt-2">
            <div className="panel panel-default">
                <div className="panel-body">
                    <p className="badge bg-secondary">
                        Accounts details for account: <strong>{accountId}</strong>
                    </p>

                    <table className="table table-striped table-bordered">
                        <thead className="thead-dark">
                            <tr>
                                <th>Account Id</th>
                                <th>Balance</th>
                                <th>Account Name</th>
                                <th>Iban</th>
                                <th>HolderName</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>{account['account-id']}</td>
                                <td>{account['balance']}</td>
                                <td>{account['account-name']}</td>
                                <td>{account['iban']}</td>
                                <td>{account['holder-name']}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            
        </div>
    )
}

export default AccountComponent