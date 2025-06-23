CREATE TABLE TB_PROFESSOR (
    id_professor INT PRIMARY KEY IDENTITY(1,1),
    nome_completo VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    titulacao VARCHAR(100),
    login VARCHAR(50) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL, 
    perfil VARCHAR(50) DEFAULT 'PROFESSOR'
);

CREATE TABLE TB_ALUNO (
    id_aluno INT PRIMARY KEY IDENTITY(1001,1),
    matricula VARCHAR(50) UNIQUE NOT NULL,
    nome_completo VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    data_nascimento DATE,
    login VARCHAR(50) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    perfil VARCHAR(50) DEFAULT 'ALUNO'
);


CREATE TABLE TB_USUARIO_ADMIN (
    id_usuario INT PRIMARY KEY IDENTITY(1,1),
    nome_completo VARCHAR(255) NOT NULL,
    login VARCHAR(50) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    perfil VARCHAR(50) NOT NULL 
);


CREATE TABLE TB_CURSO (
    id_curso INT PRIMARY KEY IDENTITY(1,1),
    nome VARCHAR(255) NOT NULL,
    codigo_curso VARCHAR(20) UNIQUE NOT NULL,
    descricao TEXT,
    fk_coordenador INT,
    FOREIGN KEY (fk_coordenador) REFERENCES TB_PROFESSOR(id_professor)
);

CREATE TABLE TB_DISCIPLINA (
    id_disciplina INT PRIMARY KEY IDENTITY(1,1),
    nome VARCHAR(255) NOT NULL,
    codigo VARCHAR(20) UNIQUE NOT NULL,
    ementa TEXT,
    carga_horaria INT NOT NULL
);

CREATE TABLE TB_TURMA (
    id_turma INT PRIMARY KEY IDENTITY(1,1),
    semestre VARCHAR(10) NOT NULL, 
    horario VARCHAR(255),
    fk_professor INT NOT NULL,
    fk_disciplina INT NOT NULL,
    FOREIGN KEY (fk_professor) REFERENCES TB_PROFESSOR(id_professor),
    FOREIGN KEY (fk_disciplina) REFERENCES TB_DISCIPLINA(id_disciplina)
);

CREATE TABLE TB_MATRICULA (
    id_matricula INT PRIMARY KEY IDENTITY(1,1),
    fk_aluno INT NOT NULL,
    fk_turma INT NOT NULL,
    media_final DECIMAL(4, 2) DEFAULT 0.0,
    frequencia_percentual DECIMAL(5, 2) DEFAULT 100.0,
    status VARCHAR(50) DEFAULT 'CURSANDO',
    FOREIGN KEY (fk_aluno) REFERENCES TB_ALUNO(id_aluno),
    FOREIGN KEY (fk_turma) REFERENCES TB_TURMA(id_turma),
    UNIQUE (fk_aluno, fk_turma) 
);


CREATE TABLE TB_GRADE_CURRICULAR (
    fk_curso INT,
    fk_disciplina INT,
    PRIMARY KEY (fk_curso, fk_disciplina),
    FOREIGN KEY (fk_curso) REFERENCES TB_CURSO(id_curso),
    FOREIGN KEY (fk_disciplina) REFERENCES TB_DISCIPLINA(id_disciplina)
);
