import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import {useNavigate} from 'react-router-dom';
import { useState } from 'react'
import Container from 'react-bootstrap/Container';
import axios from 'axios';

const CreateJha = () => {
    const navigate = useNavigate()
    const [title, setTitle] = useState('');
    const [author, setAuthor] = useState('');

    const makeNewJha = async () => {
        // eslint-disable-next-line no-restricted-globals
        event.preventDefault();
        const response = await axios.post('http://localhost:8080/jha/add', null, {params: { title: title, author: author }});
        try{
            navigate("/")
        } catch (err) {
            console.log("The response message is ", err)
            alert("Title is already being used, please change");
        }
    }

  return (
    <Container>
        <Form onSubmit={makeNewJha}>
      <Form.Group className="mb-3">
        <Form.Label>JHA Title</Form.Label>
        <Form.Control type="text" required={true} value={title} placeholder="Enter the title of new Job Hazard Analysis" onChange={(event) => setTitle(event.target.value)}/>
        <Form.Text className="text-muted">
          Each JHA title must be unique
        </Form.Text>
      </Form.Group>

      <Form.Group className="mb-3">
        <Form.Label>Author</Form.Label>
        <Form.Control type="text" required={true} value={author} placeholder="Enter the name of JHA Author" onChange={(event) => setAuthor(event.target.value)}/>
      </Form.Group>
      <Button variant="primary" type="submit">
        Submit
      </Button>
    </Form>
    </Container>
    
  );
}

export default CreateJha;