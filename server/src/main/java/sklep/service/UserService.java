package sklep.service;

import org.springframework.web.multipart.MultipartFile;
import sklep.config.Constants;
import sklep.entity.Address;
import sklep.entity.CompanyAddress;
import sklep.entity.Role;
import sklep.entity.User;
import sklep.repository.*;
import sklep.service.dto.*;

import sklep.service.dto.Create.CreateAddressDTO;
import sklep.service.dto.Create.CreateCompanyAddressDTO;
import sklep.service.dto.Create.RegisterDTO;
import sklep.service.dto.Update.UpdateAddressDTO;
import sklep.service.dto.Update.UpdateCompanyAddressDTO;
import sklep.service.dto.Update.UpdateUserDTO;
import sklep.service.exception.EntityNotFoundException;
import sklep.service.exception.ForbiddenException;
import sklep.service.mapper.AddressMapper;
import sklep.service.mapper.CompanyAddressMapper;
import sklep.service.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sklep.utils.FileUploadUtil;
import sklep.utils.ObjectOverwrite;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserService {
    public static final String uploadDir = "photo/user/";

    private final static Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SecurityService securityService;
    private final AddressMapper addressMapper;
    private final AddressRepository addressRepository;
    private final CompanyAddressMapper companyAddressMapper;
    private final CompanyAddressRepository companyAddressRepository;


    @Autowired
    public UserService(UserMapper userMapper,
                       PasswordEncoder passwordEncoder,
                       UserRepository userRepository,
                       RoleRepository roleRepository,
                       SecurityService securityService,
                       AddressMapper addressMapper,
                       AddressRepository addressRepository,
                       CompanyAddressMapper companyAddressMapper,
                       CompanyAddressRepository companyAddressRepository){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.securityService = securityService;
        this.addressMapper = addressMapper;
        this.addressRepository = addressRepository;
        this.companyAddressMapper = companyAddressMapper;
        this.companyAddressRepository = companyAddressRepository;
    }

    public void save(RegisterDTO registerDTO){
        log.debug("Saving user : {}", registerDTO);

        User user = userMapper.toUserFromRegisterDTO(registerDTO);
        Role userRole = roleRepository.findByName(Constants.Role.USER.name);
        user.setRole(userRole);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    public List<UserDTO> getUsers(){
        log.debug("Fetching all users");

        List<User> users = userRepository.findAll();

        return userMapper.toDto(users);
    }

    public boolean existsByName(String name){
        log.debug("Checking if exists user by name: {}", name);

        return userRepository.existsByName(name);
    }

    public UserDTO getUserByEmail(String email){
        log.debug("Fetching user by email: {}", email);

        User user = this.userRepository.findByEmail(email);

        return this.userMapper.toDto(user);
    }

    public UserDTO getUserById(Long id){
        log.debug("Fetching user by id: {}", id);

        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with requested id doesn't exist."));

        return this.userMapper.toDto(user);
    }


    public void deleteUser(Long id, User authenticatedUser){
        log.debug("Deleting user {}", id);

        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with requested id doesn't exist."));
        securityService.checkPermission(authenticatedUser,id);

        userRepository.delete(user);
    }

    public void makeAdmin(Long id){
        log.debug("Making admin user {}", id);

        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with requested id doesn't exist."));
        Role adminRole = roleRepository.findByName(Constants.Role.ADMIN.name);

        userRepository.save(user);
    }

    public void removeAdmin(Long id){
        log.debug("Removing admin user {}", id);

        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with requested id doesn't exist."));
        Role adminRole = roleRepository.findByName(Constants.Role.ADMIN.name);

        userRepository.save(user);
    }


    public boolean existsByEmail(String email) {
        log.debug("Checking if exists user by name: {}", email);

        return userRepository.existsByEmail(email);

    }
    public UserDTO updateUser(Long id, UpdateUserDTO newUserDTO, User authenticatedUser){
        log.debug("Updating user {} to {}", id, newUserDTO);

        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with requested id doesn't exist."));

        if(authenticatedUser==null){
            throw new ForbiddenException("zaloguj się--updateUser--ProductService");
        }
        securityService.checkPermission(authenticatedUser,id);

        User updatedUser = userMapper.toUserFromUpdateUserDTO(newUserDTO);
        ObjectOverwrite.map(user, updatedUser);

        if(newUserDTO.getPassword()!=null){
            user.setPassword(passwordEncoder.encode(newUserDTO.getPassword()));
        }

        return this.userMapper.toDto(userRepository.save(user));
    }

    public UserDTO updateUserPhoto(Long id , User authenticatedUser, MultipartFile file){
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with requested id doesn't exist."));

        if(authenticatedUser==null){
            throw new ForbiddenException("zaloguj się--updateUser--ProductService");
        }
        securityService.checkPermission(authenticatedUser,id);

        String fileName = FileUploadUtil.updateFile(uploadDir, file, user.getPhotoName());
        user.setPhoto(fileName);

        return this.userMapper.toDto(userRepository.save(user));
    }

    public AddressDTO saveAddress(Long id, CreateAddressDTO createAddressDTO, User authenticatedUser){
        log.debug("Saving userAddres : {}", createAddressDTO);

        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with requested id doesn't exist."));
        if(authenticatedUser==null){
            throw new ForbiddenException("zaloguj się--updateUser--ProductService");
        }
        securityService.checkPermission(authenticatedUser,id);

        if(user.getAddress()!=null){
            throw new ForbiddenException("address already exist--updateUser--ProductService");
        }

        Address address = addressMapper.toAddressFromCreateDTO(createAddressDTO);

        address.setCity(createAddressDTO.getCity());
        address.setApartmentNumber(createAddressDTO.getApartmentNumber());
        address.setStreet(createAddressDTO.getStreet());
        address.setHouseNumber(createAddressDTO.getHouseNumber());
        address.setZipCode(createAddressDTO.getZipCode());
        address.setUser(user);

        address = addressRepository.save(address);
        return addressMapper.toDto(address);


    }

    public CompanyAddressDTO saveCompanyAddress(Long id, CreateCompanyAddressDTO createCompanyAddressDTO, User authenticatedUser){
        log.debug("Saving userAddres : {}", createCompanyAddressDTO);

        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with requested id doesn't exist."));
        if(authenticatedUser==null){
            throw new ForbiddenException("zaloguj się--updateUser--ProductService");
        }
        securityService.checkPermission(authenticatedUser,id);

        CompanyAddress companyAddress = companyAddressMapper.toCompanyAddressFromCreateDTO(createCompanyAddressDTO);

        companyAddress.setUser(user);

        companyAddress = companyAddressRepository.save(companyAddress);
        return companyAddressMapper.toDto(companyAddress);


    }

    public AddressDTO getUserAddressById(Long id, Long idAddress){
        log.debug("Fetching user{} address by id: {}", id,idAddress);

        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with requested id doesn't exist."));

        Address address = this.addressRepository.findById(idAddress)
                .orElseThrow(() -> new EntityNotFoundException("address with requested id doesn't exist."));

        return this.addressMapper.toDto(address);
    }

    public CompanyAddressDTO getCompanyAddressById(Long id, Long idCompanyAddress){
        log.debug("Fetching user{} address by id: {}", id, idCompanyAddress);

        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with requested id doesn't exist."));

        CompanyAddress companyAddress = this.companyAddressRepository.findById(idCompanyAddress)
                .orElseThrow(() -> new EntityNotFoundException("Company Address with requested id doesn't exist."));

        return this.companyAddressMapper.toDto(companyAddress);
    }

    public void updateAddress(Long id, UpdateAddressDTO newAddressDTO, User authenticatedUser, Long idAddress){
        log.debug("Updating address {} to {}", idAddress, newAddressDTO);

        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with requested id doesn't exist."));

        Address address = this.addressRepository.findById(idAddress)
                .orElseThrow(() -> new EntityNotFoundException("address with requested id doesn't exist."));

        if(authenticatedUser==null){
            throw new ForbiddenException("zaloguj się--updateUser--ProductService");
        }
        securityService.checkPermission(authenticatedUser,id);
        securityService.checkPermission(authenticatedUser,address.getUser().getId());

        Address updatedAddress = addressMapper.toAddressFromUpdateAddressDTO(newAddressDTO);
        ObjectOverwrite.map(address, updatedAddress);

        addressRepository.save(address);

    }

    public void updateCompanyAddress(Long id, UpdateCompanyAddressDTO newCompanyAddressDTO, User authenticatedUser, Long idCompanyAddress){
        log.debug("Updating  company address {} to {}", idCompanyAddress, newCompanyAddressDTO);

        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with requested id doesn't exist."));

        CompanyAddress companyAddress = this.companyAddressRepository.findById(idCompanyAddress)
                .orElseThrow(() -> new EntityNotFoundException("company address with requested id doesn't exist."));

        if(authenticatedUser==null){
            throw new ForbiddenException("zaloguj się--updateUser--ProductService");
        }
        securityService.checkPermission(authenticatedUser,id);
        securityService.checkPermission(authenticatedUser,companyAddress.getUser().getId());

        CompanyAddress updatedCompanyAddress = companyAddressMapper.toCompanyAddressFromUpdateCompanyAddressDTO(newCompanyAddressDTO);
        ObjectOverwrite.map(companyAddress, updatedCompanyAddress);

        companyAddressRepository.save(companyAddress);

    }

    public void deleteAddress(Long id, User authenticatedUser, Long idAddress){
        log.debug("Deleting address {}", idAddress);

        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with requested id doesn't exist."));
        securityService.checkPermission(authenticatedUser,id);

        Address address = this.addressRepository.findById(idAddress)
                .orElseThrow(() -> new EntityNotFoundException("address with requested id doesn't exist."));
        securityService.checkPermission(authenticatedUser, address.getUser().getId());

        addressRepository.delete(address);
    }

    public void deleteCompanyAddress(Long id, User authenticatedUser, Long idCompanyAddress){
        log.debug("Deleting CompanyAddress {}", idCompanyAddress);

        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with requested id doesn't exist."));
        securityService.checkPermission(authenticatedUser,id);

        CompanyAddress companyAddress = this.companyAddressRepository.findById(idCompanyAddress)
                .orElseThrow(() -> new EntityNotFoundException("Company Address with requested id doesn't exist."));
        securityService.checkPermission(authenticatedUser, companyAddress.getUser().getId());

        companyAddressRepository.delete(companyAddress);
    }
}
