-- Second migration: Insert default/seed data
-- Populates initial data for pet_owner, pet, and veterinary_appointment tables

-- Insert default pet owners
INSERT INTO pet_owner (id, name, email) VALUES
    ('owner-001', 'Alice Johnson', 'alice.johnson@email.com'),
    ('owner-002', 'Bob Smith', 'bob.smith@email.com'),
    ('owner-003', 'Carol Davis', 'carol.davis@email.com'),
    ('owner-004', 'David Wilson', 'david.wilson@email.com'),
    ('owner-005', 'Emma Brown', 'emma.brown@email.com');

-- Insert default pets
INSERT INTO pet (id, pet_owner_id, name, breed, birth_date, picture_url) VALUES
    ('pet-001', 'owner-001', 'Max', 'Golden Retriever', '2020-03-15', 'https://example.com/images/max.jpg'),
    ('pet-002', 'owner-001', 'Luna', 'Border Collie', '2021-07-22', 'https://example.com/images/luna.jpg'),
    ('pet-003', 'owner-002', 'Charlie', 'Labrador', '2019-11-08', 'https://example.com/images/charlie.jpg'),
    ('pet-004', 'owner-003', 'Bella', 'German Shepherd', '2022-01-12', 'https://example.com/images/bella.jpg'),
    ('pet-005', 'owner-003', 'Rocky', 'Bulldog', '2020-09-03', 'https://example.com/images/rocky.jpg'),
    ('pet-006', 'owner-004', 'Daisy', 'Poodle', '2021-04-17', 'https://example.com/images/daisy.jpg'),
    ('pet-007', 'owner-005', 'Milo', 'Beagle', '2019-12-25', 'https://example.com/images/milo.jpg'),
    ('pet-008', 'owner-005', 'Coco', 'Shih Tzu', '2022-05-30', 'https://example.com/images/coco.jpg');

-- Insert default veterinary appointments
INSERT INTO veterinary_appointment (id, pet_id, date, type) VALUES
    ('appt-001', 'pet-001', '2024-01-15T10:00:00', 'Consultation'),
    ('appt-002', 'pet-001', '2024-02-20T14:30:00', 'Vaccination'),
    ('appt-003', 'pet-002', '2024-01-22T09:15:00', 'Consultation'),
    ('appt-004', 'pet-003', '2024-01-18T11:45:00', 'Vaccination'),
    ('appt-005', 'pet-003', '2024-02-10T16:00:00', 'Consultation'),
    ('appt-006', 'pet-004', '2024-01-25T13:20:00', 'Consultation'),
    ('appt-007', 'pet-005', '2024-02-05T08:30:00', 'Vaccination'),
    ('appt-008', 'pet-006', '2024-01-30T15:45:00', 'Consultation'),
    ('appt-009', 'pet-007', '2026-02-12T10:15:00', 'Vaccination'),
    ('appt-010', 'pet-008', '2026-02-15T14:00:00', 'Consultation'),
    ('appt-011', 'pet-001', '2026-03-10T11:30:00', 'Consultation'),
    ('appt-012', 'pet-004', '2026-03-05T09:45:00', 'Vaccination');
