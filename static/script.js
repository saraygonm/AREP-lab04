document.addEventListener('DOMContentLoaded', () => {
    // Restringir la selección de fechas
    const fechaInput = document.getElementById('fecha');
    if (fechaInput) {
        const today = new Date();
        const year = today.getFullYear();
        const month = String(today.getMonth() + 1).padStart(2, '0'); // Asegura formato MM
        const day = String(today.getDate()).padStart(2, '0'); // Asegura formato DD

        const minDate = `${year}-${month}-${day}`;
        fechaInput.setAttribute('min', minDate);
    }

    // Asignar eventos a los botones
    document.getElementById('añadirReserva').addEventListener('click', () => {
        const nombre = document.getElementById('nombre').value.trim();
        const fecha = document.getElementById('fecha').value;
        const hora = document.getElementById('hora').value;
        const tarjeta = document.getElementById('card-number').value.trim();

        if (nombre && fecha && hora && tarjeta) {
            fetch('/api/services', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ nombre, fecha, hora, tarjeta })
            })
            .then(response => response.json())
            .then(result => {
                if (result.status === 'success') {
                    alert('Reserva agregada exitosamente');
                    document.getElementById('reservation-form').reset();
                } else {
                    alert('Error al agregar la reserva.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error en la conexión al servidor.');
            });
        } else {
            alert('Por favor, completa todos los campos.');
        }
    });

    document.getElementById('fetchTodos').addEventListener('click', () => {
        fetch('/api/services')
            .then(response => response.json())
            .then(data => {
                console.log("Datos recibidos:", data); //  Depuración

                const tableBody = document.getElementById('reservas-table');
                tableBody.innerHTML = '';

                if (data.services.length === 0) {
                    tableBody.innerHTML = '<tr><td colspan="5">No hay reservas registradas</td></tr>';
                    return;
                }

                data.services.forEach((reserva, index) => {
                    const [nombre, fecha, hora, tarjeta] = reserva.split(', ');

                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${index + 1}</td>
                        <td>${nombre}</td>
                        <td>${fecha}</td>
                        <td>${hora}</td>
                        <td><button class="delete-btn" data-index="${index}">Eliminar</button></td>
                    `;
                    tableBody.appendChild(row);
                });

                //  Agregar evento a los botones de eliminar después de generar la tabla
                document.querySelectorAll('.delete-btn').forEach(button => {
                    button.addEventListener('click', (event) => {
                        const index = event.target.getAttribute('data-index');
                        console.log("Intentando eliminar reserva en posición:", index); //depuración
                        deleteReserva(index);
                    });
                });
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error en la conexión al servidor.');
            });
    });


    document.getElementById('clearTodos').addEventListener('click', () => {
        fetch('/api/services/clear', {
            method: 'DELETE'
        })
        .then(response => response.json())
        .then(result => {
            if (result.status === 'success') {
                alert('Todas las reservas fueron eliminadas');
                document.getElementById('reservas-table').innerHTML = '';
            } else {
                alert('Error al eliminar las reservas.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error en la conexión al servidor.');
        });
    });

    document.getElementById('fetchImage').addEventListener('click', (event) => {
        event.preventDefault();

        const imageContainer = document.querySelector('.image-container');
        let existingImage = imageContainer.querySelector('img');

        if (existingImage) {
            imageContainer.removeChild(existingImage);
        }

        fetch('/api/imgTarjeta')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error al cargar la imagen');
                }
                return response.blob();
            })
            .then(blob => {
                const url = URL.createObjectURL(blob);
                const img = document.createElement('img');
                img.src = url;
                img.alt = "Tarjeta";
                img.onload = () => URL.revokeObjectURL(url);
                imageContainer.appendChild(img);
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error al cargar la imagen.');
            });
    });

     function deleteReserva(index) {
         console.log("Intentando eliminar reserva en índice:", index); // Depuración

         fetch(`/api/services/${index}`, {
             method: 'DELETE',
             headers: {
                 'Accept': 'application/json',
                 'Content-Type': 'application/json'
             }
         })
         .then(response => response.json())
         .then(result => {
             console.log("Respuesta del servidor:", result);
             if (result.status === 'success') {
                 alert('Reserva eliminada correctamente');
                 document.getElementById('fetchTodos').click(); // Refrescar la lista de reservas
             } else {
                 alert('Error al eliminar la reserva.');
             }
         })
         .catch(error => {
             console.error('Error:', error);
             alert('Error en la conexión al servidor.');
         });
     }


});
