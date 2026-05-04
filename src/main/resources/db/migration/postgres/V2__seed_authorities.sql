INSERT INTO roles (name, description) VALUES
('ADMIN','System administrator with full access'),
('EMPLOYER','Employer who can post jobs and manage candidates'),
('CANDIDATE','Job seeker who can apply for jobs');


INSERT INTO permissions (name, description, module, action) VALUES
('user:view',           'View user list and details',           'USER',         'VIEW'),
('user:create',         'Create new user',                      'USER',         'CREATE'),
('user:update',         'Update user information',              'USER',         'UPDATE'),
('user:delete',         'Delete user',                          'USER',         'DELETE'),
('user:approve',        'Approve recruiter account',            'USER',         'APPROVE'),

('job:create',          'Create new job posting',               'JOB',          'CREATE'),
('job:update',          'Update job posting',                   'JOB',          'UPDATE'),
('job:delete',          'Delete job posting',                   'JOB',          'DELETE'),
('job:manage_all',      'Manage all job postings (admin)',      'JOB',          'MANAGE_ALL'),

('application:view',    'View applications',                    'APPLICATION',  'VIEW'),
('application:apply',   'Submit job application',               'APPLICATION',  'APPLY'),
('application:review',  'Review and evaluate application',      'APPLICATION',  'REVIEW'),
('application:delete',  'Delete application',                   'APPLICATION',  'DELETE'),

('profile:view',        'View own profile',                     'PROFILE',      'VIEW'),
('profile:update',      'Update own profile',                   'PROFILE',      'UPDATE'),

('payment:create',      'Create payment transaction',           'PAYMENT',      'CREATE'),
('payment:view',        'View own payment history',             'PAYMENT',      'VIEW'),
('payment:view_all',    'View all payment transactions',        'PAYMENT',      'VIEW_ALL'),

('report:view_own',     'View own statistics (recruiter)',      'REPORT',       'VIEW_OWN'),
('report:view_all',     'View system-wide reports (admin)',     'REPORT',       'VIEW_ALL');


INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'ADMIN';


INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name IN (
    'job:create',
    'job:update',
    'job:delete',
    'application:view',
    'application:review',
    'profile:view',
    'profile:update',
    'payment:create',
    'payment:view',
    'report:view_own'
)
WHERE r.name = 'RECRUITER';


INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name IN (
    'application:apply',
    'application:view',
    'profile:view',
    'profile:update',
    'payment:create',
    'payment:view'
)
WHERE r.name = 'CANDIDATE';