import { useState, useEffect } from 'react'
import axios from 'axios'
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Container from 'react-bootstrap/Container';
import '../styles/EditJha.css'


const SafeGuards = (props) => {
    const [safeGuards, setSafeGuards] = useState([]);
    const [newSafeGuard, setNewSafeGuard] = useState('');

    const makeNewSafeGuard = async () => {
        // eslint-disable-next-line no-restricted-globals
        event.preventDefault();
        const response = await axios.post(`http://localhost:8080/safeGuard/${props.taskID}`, null, {params: { safetyPrecaution: newSafeGuard}});
        try {
            const addNewSafeGuard = safeGuards.concat(response.data);
            setSafeGuards(addNewSafeGuard);
            setNewSafeGuard('');
        } catch (err) {
            console.log("The response message is ", response.message)
        }
    }

    const deleteSafeGuard = async (safeGuardID) => {
        const response = await axios.delete(`http://localhost:8080/safeGuard/${safeGuardID}`);
        try {
            if (response.status === 204) {
                const newSafeGuards = safeGuards.filter(safeGuard => {
                    if (safeGuard.safeGuardID !== safeGuardID){
                        return safeGuard;
                    }
                });
                setSafeGuards(newSafeGuards);
            }
        }
        catch (err) {
            console.log("Error with deleting Task: ", err);
        }
    }

    const setUpdateInfo = (safeGuardID) => {
        props.setUpdateType();
        props.setUpdateID(safeGuardID);
        props.visibility(true);
    }

    useEffect(() => {
        const getSafeGuards = async () => {
            const response = await axios.get(`http://localhost:8080/safeGuard/${props.taskID}`)
            try {
                const hazardSafeGuards =response.data.map(safeGuard => {
                    return safeGuard;
                })
                setSafeGuards(hazardSafeGuards);
            } catch (err){
                console.log("Error retrieving safeGuards", err);
            }
        }
        getSafeGuards();
    }, [safeGuards]);

    return (
        <Container>
            <Row className="d-flex flex-wrap">
                <p className="mb-0"><b>SafeGuards:</b></p>
                <ul>  
                    {safeGuards.map(safeGuard => {
                        return  (<div><Button className="btn btn-link rounded-circle" variant="link" onClick={() => setUpdateInfo(safeGuard.safeGuardID)}><i class="fas fa-regular fa-file-pen fa-2xs"></i></Button>
                        <li>
                            {safeGuard.safetyPrecaution}
                        </li>
                        <Button className="btn btn-link rounded-circle text-danger" id="deleteButton" variant="link" onClick={() => deleteSafeGuard(safeGuard.safeGuardID)}><i class="fas fa-solid fa-x fa-2xs"></i></Button>
                        </div>)
                    })}
                </ul>
            </Row>
            <Row>
                <Form onSubmit={makeNewSafeGuard}>
                    <Row>
                        <Col>
                            <Form.Group className="mb-3">
                                <Form.Control type="text" required={true} value={newSafeGuard} placeholder="Enter New SafeGuard" onChange={(event) => setNewSafeGuard(event.target.value)}/>
                            </Form.Group>
                        </Col>
                    </Row>
                </Form>
            </Row>
        </Container>
    )
}

export default SafeGuards;