import './App.css';
import { useEffect, useState } from 'react';
import axios from 'axios';
import { Edit, Trash2 } from 'lucide-react';
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";

const API_URL = "http://localhost:8080/api/employees";

function App() {

  //etat pour stocker la liste des employes
  const [employees, setEmployees] = useState([]);
  const [formData, setFormData] = useState({
    firstName: '', 
    lastName: '', 
    email: '', 
    position: '',
    department: '', 
    salary: '', 
    hireDate: ''
  });
  const [editingEmployee, setEditingEmployee] = useState(null);

  //s'execute au demarrage
  useEffect(() => {
    fetchEmployees();
  }, []);

  //recupere la liste des employes
  const fetchEmployees = async () => {
    try {
      const response = await axios.get(API_URL);
      setEmployees(response.data);
      console.log('Employés chargé: ', response.data);
    } catch (error) {
      console.error('Erreur: ', error);
    }
  };

  const handleInputChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    })
  }

  const handleSubmit = async (e) => {
    e.preventDefault(); //empeche le rechargement de la page

    try {
      const dataToSend = {
        ...formData,
        salary: parseFloat(formData.salary)
      };

      if (editingEmployee) {
        await axios.put(`${API_URL}/${editingEmployee}`, dataToSend);
        setEditingEmployee(null);
      } else {
        await axios.post(API_URL, dataToSend);
      }

      //vide le formulaire apres l'envoie des donnees
      setFormData({
        firstName: '', 
        lastName: '', 
        email: '', 
        position: '',
        department: '', 
        salary: '', 
        hireDate: ''
      });

      fetchEmployees();
      console.log('Employé créé avec succès.')
    } catch (error) {
      console.error('Erreur lors de la création: ', error);
    }
  }

  const handleUpdate = (employee) => {
    setFormData({
      firstName: employee.firstName, 
        lastName: employee.lastName, 
        email: employee.email, 
        position: employee.position,
        department: employee.department, 
        salary: employee.salary, 
        hireDate: employee.hireDate
    });
    setEditingEmployee(employee.id);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Êtes vous sûr de vouloir supprimer cet employés ? ')) {
      try {
        await axios.delete(`${API_URL}/${id}`);
        fetchEmployees();
        console.log('Employé supprimé :', id);
      } catch (error) {
        console.error('Erreur lors de la suppression: ', error);
      }
    }
  }

  return (
    <div className="App">
      <header className='app-header'>
        <h1>Système de gestion RH</h1>
      </header>

      <main className='app-main'>
        <section className='form-section'>
          <h2>Ajouter un employé</h2>
          <form onSubmit={handleSubmit}>
          <input
            type="text"
            name="firstName"
            placeholder="Prénom"
            value={formData.firstName}
            onChange={handleInputChange}
          />
          <input
            type="text"
            name="lastName"
            placeholder="Nom"
            value={formData.lastName}
            onChange={handleInputChange}
          />
          <input
            type="email"
            name="email"
            placeholder="Email"
            value={formData.email}
            onChange={handleInputChange}
          />
          <input
            type="text"
            name="position"
            placeholder="Poste"
            value={formData.position}
            onChange={handleInputChange}
          />
          <input
            type="text"
            name="department"
            placeholder="Département"
            value={formData.department}
            onChange={handleInputChange}
          />
          <input
            type="number"
            name="salary"
            placeholder="Salaire"
            value={formData.salary}
            onChange={handleInputChange}
          />
          <DatePicker
            selected={formData.hireDate ? new Date(formData.hireDate) : null}
            onChange={(date) => setFormData({
              ...formData,
              hireDate: date ? date.toISOString().split('T')[0] : ''
            })}
            placeholderText="Date d'embauche"
            dateFormat="dd/MM/yyyy"
            className="form-input"
            showPopperArrow={false}
            required
          />
          <button type="submit">
            {editingEmployee ? 'Modifier' : 'Ajouter'}
          </button>
        </form>
        </section>

        <section className='employees-section'>
          <h2>Employés ({employees.length})</h2>
          {employees.map(employee => (
            <div key={employee.id} className="employee-card">
              <div className="employee-info">
                <div className="employee-name">
                  {employee.firstName} {employee.lastName}
                </div>
                <div className="employee-details">
                  <span className="employee-position">{employee.position}</span>
                  <span className="employee-department">{employee.department}</span>
                </div>
              </div>
              <div className="employee-actions">
                <button 
                  onClick={() => handleUpdate(employee)}
                  className="action-btn edit-btn"
                  title="Modifier"
                >
                  <Edit size={16} />
                </button>
                <button 
                  onClick={() => handleDelete(employee.id)}
                  className="action-btn delete-btn"
                  title="Supprimer"
                >
                  <Trash2 size={16} />
                </button>
              </div>
            </div>
          ))}
        </section>

      </main>
    </div>
  );
}

export default App;
