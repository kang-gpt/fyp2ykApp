package com.yk.booking.web.rest;

import static com.yk.booking.domain.ClientTierAsserts.*;
import static com.yk.booking.web.rest.TestUtil.createUpdateProxyForBean;
import static com.yk.booking.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yk.booking.IntegrationTest;
import com.yk.booking.domain.ClientTier;
import com.yk.booking.repository.ClientTierRepository;
import com.yk.booking.service.dto.ClientTierDTO;
import com.yk.booking.service.mapper.ClientTierMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ClientTierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClientTierResourceIT {

    private static final String DEFAULT_TIER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TIER_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_DISCOUNT_PERCENTAGE = new BigDecimal(1);
    private static final BigDecimal UPDATED_DISCOUNT_PERCENTAGE = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/client-tiers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ClientTierRepository clientTierRepository;

    @Autowired
    private ClientTierMapper clientTierMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClientTierMockMvc;

    private ClientTier clientTier;

    private ClientTier insertedClientTier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClientTier createEntity() {
        return new ClientTier().tierName(DEFAULT_TIER_NAME).discountPercentage(DEFAULT_DISCOUNT_PERCENTAGE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClientTier createUpdatedEntity() {
        return new ClientTier().tierName(UPDATED_TIER_NAME).discountPercentage(UPDATED_DISCOUNT_PERCENTAGE);
    }

    @BeforeEach
    void initTest() {
        clientTier = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedClientTier != null) {
            clientTierRepository.delete(insertedClientTier);
            insertedClientTier = null;
        }
    }

    @Test
    @Transactional
    void createClientTier() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ClientTier
        ClientTierDTO clientTierDTO = clientTierMapper.toDto(clientTier);
        var returnedClientTierDTO = om.readValue(
            restClientTierMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientTierDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ClientTierDTO.class
        );

        // Validate the ClientTier in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedClientTier = clientTierMapper.toEntity(returnedClientTierDTO);
        assertClientTierUpdatableFieldsEquals(returnedClientTier, getPersistedClientTier(returnedClientTier));

        insertedClientTier = returnedClientTier;
    }

    @Test
    @Transactional
    void createClientTierWithExistingId() throws Exception {
        // Create the ClientTier with an existing ID
        clientTier.setId(1L);
        ClientTierDTO clientTierDTO = clientTierMapper.toDto(clientTier);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientTierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientTierDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ClientTier in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTierNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        clientTier.setTierName(null);

        // Create the ClientTier, which fails.
        ClientTierDTO clientTierDTO = clientTierMapper.toDto(clientTier);

        restClientTierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientTierDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClientTiers() throws Exception {
        // Initialize the database
        insertedClientTier = clientTierRepository.saveAndFlush(clientTier);

        // Get all the clientTierList
        restClientTierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clientTier.getId().intValue())))
            .andExpect(jsonPath("$.[*].tierName").value(hasItem(DEFAULT_TIER_NAME)))
            .andExpect(jsonPath("$.[*].discountPercentage").value(hasItem(sameNumber(DEFAULT_DISCOUNT_PERCENTAGE))));
    }

    @Test
    @Transactional
    void getClientTier() throws Exception {
        // Initialize the database
        insertedClientTier = clientTierRepository.saveAndFlush(clientTier);

        // Get the clientTier
        restClientTierMockMvc
            .perform(get(ENTITY_API_URL_ID, clientTier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(clientTier.getId().intValue()))
            .andExpect(jsonPath("$.tierName").value(DEFAULT_TIER_NAME))
            .andExpect(jsonPath("$.discountPercentage").value(sameNumber(DEFAULT_DISCOUNT_PERCENTAGE)));
    }

    @Test
    @Transactional
    void getNonExistingClientTier() throws Exception {
        // Get the clientTier
        restClientTierMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClientTier() throws Exception {
        // Initialize the database
        insertedClientTier = clientTierRepository.saveAndFlush(clientTier);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the clientTier
        ClientTier updatedClientTier = clientTierRepository.findById(clientTier.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedClientTier are not directly saved in db
        em.detach(updatedClientTier);
        updatedClientTier.tierName(UPDATED_TIER_NAME).discountPercentage(UPDATED_DISCOUNT_PERCENTAGE);
        ClientTierDTO clientTierDTO = clientTierMapper.toDto(updatedClientTier);

        restClientTierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clientTierDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientTierDTO))
            )
            .andExpect(status().isOk());

        // Validate the ClientTier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedClientTierToMatchAllProperties(updatedClientTier);
    }

    @Test
    @Transactional
    void putNonExistingClientTier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientTier.setId(longCount.incrementAndGet());

        // Create the ClientTier
        ClientTierDTO clientTierDTO = clientTierMapper.toDto(clientTier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientTierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clientTierDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientTierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientTier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClientTier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientTier.setId(longCount.incrementAndGet());

        // Create the ClientTier
        ClientTierDTO clientTierDTO = clientTierMapper.toDto(clientTier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientTierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientTierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientTier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClientTier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientTier.setId(longCount.incrementAndGet());

        // Create the ClientTier
        ClientTierDTO clientTierDTO = clientTierMapper.toDto(clientTier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientTierMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientTierDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClientTier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClientTierWithPatch() throws Exception {
        // Initialize the database
        insertedClientTier = clientTierRepository.saveAndFlush(clientTier);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the clientTier using partial update
        ClientTier partialUpdatedClientTier = new ClientTier();
        partialUpdatedClientTier.setId(clientTier.getId());

        partialUpdatedClientTier.discountPercentage(UPDATED_DISCOUNT_PERCENTAGE);

        restClientTierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClientTier.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClientTier))
            )
            .andExpect(status().isOk());

        // Validate the ClientTier in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClientTierUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedClientTier, clientTier),
            getPersistedClientTier(clientTier)
        );
    }

    @Test
    @Transactional
    void fullUpdateClientTierWithPatch() throws Exception {
        // Initialize the database
        insertedClientTier = clientTierRepository.saveAndFlush(clientTier);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the clientTier using partial update
        ClientTier partialUpdatedClientTier = new ClientTier();
        partialUpdatedClientTier.setId(clientTier.getId());

        partialUpdatedClientTier.tierName(UPDATED_TIER_NAME).discountPercentage(UPDATED_DISCOUNT_PERCENTAGE);

        restClientTierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClientTier.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClientTier))
            )
            .andExpect(status().isOk());

        // Validate the ClientTier in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClientTierUpdatableFieldsEquals(partialUpdatedClientTier, getPersistedClientTier(partialUpdatedClientTier));
    }

    @Test
    @Transactional
    void patchNonExistingClientTier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientTier.setId(longCount.incrementAndGet());

        // Create the ClientTier
        ClientTierDTO clientTierDTO = clientTierMapper.toDto(clientTier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientTierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clientTierDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clientTierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientTier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClientTier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientTier.setId(longCount.incrementAndGet());

        // Create the ClientTier
        ClientTierDTO clientTierDTO = clientTierMapper.toDto(clientTier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientTierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clientTierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientTier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClientTier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientTier.setId(longCount.incrementAndGet());

        // Create the ClientTier
        ClientTierDTO clientTierDTO = clientTierMapper.toDto(clientTier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientTierMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(clientTierDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClientTier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClientTier() throws Exception {
        // Initialize the database
        insertedClientTier = clientTierRepository.saveAndFlush(clientTier);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the clientTier
        restClientTierMockMvc
            .perform(delete(ENTITY_API_URL_ID, clientTier.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return clientTierRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ClientTier getPersistedClientTier(ClientTier clientTier) {
        return clientTierRepository.findById(clientTier.getId()).orElseThrow();
    }

    protected void assertPersistedClientTierToMatchAllProperties(ClientTier expectedClientTier) {
        assertClientTierAllPropertiesEquals(expectedClientTier, getPersistedClientTier(expectedClientTier));
    }

    protected void assertPersistedClientTierToMatchUpdatableProperties(ClientTier expectedClientTier) {
        assertClientTierAllUpdatablePropertiesEquals(expectedClientTier, getPersistedClientTier(expectedClientTier));
    }
}
