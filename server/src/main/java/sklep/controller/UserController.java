package sklep.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sklep.entity.CompanyAddress;
import sklep.entity.User;
import sklep.service.UserService;
import sklep.service.dto.*;
import sklep.service.dto.Create.CreateAddressDTO;
import sklep.service.dto.Create.CreateCompanyAddressDTO;
import sklep.service.dto.Update.UpdateAddressDTO;
import sklep.service.dto.Update.UpdateCompanyAddressDTO;
import sklep.service.dto.Update.UpdateUserDTO;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;


@CrossOrigin
@RestController
@RequestMapping(path="/api/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(SecurityController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(
            @PathVariable final Long id
    ){
        log.debug("Request to get: user {}", id);
        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping()
    public ResponseEntity<List<UserDTO>> getUsers(
    ){
        log.debug("Request to get all users");
        List<UserDTO> userDTOs = userService.getUsers();
        return ResponseEntity.ok(userDTOs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable final Long id,
            @AuthenticationPrincipal User authenticatedUser
    ){
        log.debug("Request to delete: user {} by {}", id, authenticatedUser);
        userService.deleteUser(id,authenticatedUser);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable final Long id,
            @RequestBody @Validated UpdateUserDTO updateUserDTO,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        log.debug("Request to update: user {} by {}", id, authenticatedUser);
        UserDTO userDTO = userService.updateUser(id, updateUserDTO, authenticatedUser);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/{id}/photo")
    public ResponseEntity<UserDTO> updateUserPhoto(
            @PathVariable final Long id,
            @ModelAttribute MultipartFile file,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        log.debug("Request to update photo: user {} by {}", id, authenticatedUser);
        UserDTO userDTO = userService.updateUserPhoto(id, authenticatedUser, file);
        return ResponseEntity.ok(userDTO);
    }
//ADDRESS CONTROLLER?
    @PostMapping("/{id}/address")
    public ResponseEntity<AddressDTO> createAddress(
            @PathVariable final long id,
            @RequestBody @Validated CreateAddressDTO createAddressDTO,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        log.debug("Request to save: user Address {}", createAddressDTO);

        AddressDTO addressDTO = userService.saveAddress(id, createAddressDTO, authenticatedUser);

        //return ResponseEntity.noContent().build();
        return ResponseEntity.ok(addressDTO);
    }

    @PostMapping("/{id}/companyAddress")
    public ResponseEntity<CompanyAddressDTO> createCompanyAddress(
            @PathVariable final long id,
            @RequestBody @Validated CreateCompanyAddressDTO createCompanyAddressDTO,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        log.debug("Request to save: user  Company Address {}", createCompanyAddressDTO);

        CompanyAddressDTO companyAddressDTO = userService.saveCompanyAddress(id, createCompanyAddressDTO, authenticatedUser);

        //return ResponseEntity.noContent().build();
        return ResponseEntity.ok(companyAddressDTO);
    }

    @GetMapping("/{id}/address/{idAddress}")
    public ResponseEntity<AddressDTO> getUserAddress(
            @PathVariable final Long id,
            @PathVariable final Long idAddress
    ){
        log.debug("Request to get: user address {}", id);
        AddressDTO addressDTO = userService.getUserAddressById(id, idAddress);
        return ResponseEntity.ok(addressDTO);
    }

    @GetMapping("/{id}/companyAddress/{idCompanyAddress}")
    public ResponseEntity<CompanyAddressDTO> getCompanyAddress(
            @PathVariable final Long id,
            @PathVariable final Long idCompanyAddress
    ){
        log.debug("Request to get: user CompanyAddress {}", id);
        CompanyAddressDTO companyAddressDTO = userService.getCompanyAddressById(id, idCompanyAddress);
        return ResponseEntity.ok(companyAddressDTO);
    }

    @PutMapping("/{id}/address/{idAddress}")
    public ResponseEntity<UpdateUserDTO> updateUserAddress(
            @PathVariable final Long id,
            @PathVariable final Long idAddress,
            @RequestBody UpdateAddressDTO updateAddressDTO,
            @AuthenticationPrincipal User authenticatedUser
    ){
        log.debug("Request to update: address {} by {}", idAddress, authenticatedUser);
        userService.updateAddress(id, updateAddressDTO, authenticatedUser, idAddress);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/companyAddress/{idCompanyAddress}")
    public ResponseEntity<Void> updateCompanyAddress(
            @PathVariable final Long id,
            @PathVariable final Long idCompanyAddress,
            @RequestBody UpdateCompanyAddressDTO updateCompanyAddressDTO,
            @AuthenticationPrincipal User authenticatedUser
    ){
        log.debug("Request to update: Company Address {} by {}", idCompanyAddress, authenticatedUser);
        userService.updateCompanyAddress(id, updateCompanyAddressDTO, authenticatedUser, idCompanyAddress);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/address/{idAddress}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable final Long id,
            @PathVariable final Long idAddress,
            @AuthenticationPrincipal User authenticatedUser
    ){
        log.debug("Request to delete: address {} by {}", idAddress, authenticatedUser);

        userService.deleteAddress(id, authenticatedUser, idAddress);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/companyAddress/{idCompanyAddress}")
    public ResponseEntity<Void> deleteCompanyAddress(
            @PathVariable final Long id,
            @PathVariable final Long idCompanyAddress,
            @AuthenticationPrincipal User authenticatedUser
    ){
        log.debug("Request to delete: company address {} by {}", idCompanyAddress, authenticatedUser);

        userService.deleteCompanyAddress(id, authenticatedUser, idCompanyAddress);

        return ResponseEntity.noContent().build();
    }
}
