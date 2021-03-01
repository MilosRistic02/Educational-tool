package nl.tudelft.oopp.demo.services;

import nl.tudelft.oopp.demo.entities.Users;
import nl.tudelft.oopp.demo.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {

    private UsersRepository usersRepo;


    @Autowired
    public UsersService(UsersRepository usersRepo){
        this.usersRepo = usersRepo;

    }


    //getAllUsers

    //getAllStudents

    //getAllModerators

    //getAllLecturers

    /**
     * This methods adds a user to the database.
     * @param user
     * @return Success/Error message
     */
    public String addUser(Users user){
        if(usersRepo.existsByUsername(user.getUsername())){
            return "This user already exists!";
        }
        usersRepo.save(user);
        return "User type: " + user.getRole() +
                ", username: " + user.getUsername() +
                " has been added successfully";
    }

    /**
     * A method that deletes an already existing user from the database
     * @param user
     * @return Error message iff the user does not exists,
     * Success if the user is successfully deleted from the database.
     */
    public String deleteUser(Users user){
        if(!usersRepo.existsByUsername(user.getUsername())){
            return "This user does not exist!";
        }
        usersRepo.delete(user);
        return "User, named: " +
                user.getUsername() +
                ", has been deleted from the database";
    }

    public void deleteAllUsers(){
        usersRepo.deleteAll();
    }

    public void deleteAllStudents(){
        List<Users> usersList = usersRepo.getAllUsers();

        for(Users user : usersList){
            if(user.getRole().equals("student")){
                usersRepo.delete(user);
            }
        }
    }

    public void deleteAllModerators(){
        List<Users> usersList = usersRepo.getAllUsers();

        for(Users user : usersList){
            if(user.getRole().equals("moderator")){
                usersRepo.delete(user);
            }
        }
    }

    public void deleteAllLecturers(){
        List<Users> usersList = usersRepo.getAllUsers();

        for(Users user : usersList){
            if(user.getRole().equals("lecturer")){
                usersRepo.delete(user);
            }
        }
    }


}
