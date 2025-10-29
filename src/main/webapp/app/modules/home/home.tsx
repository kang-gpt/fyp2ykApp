import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Card, CardTitle, Button } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <div className="home-container">
      <div className="hero-section text-center">
        <h1 className="display-3 text-white">
          <Translate contentKey="home.title">Welcome to CourtEaseYK!</Translate>
        </h1>
        <p className="lead text-white">
          <Translate contentKey="home.subtitle">Your ultimate platform for booking and managing sports facilities.</Translate>
        </p>
        {account?.login ? (
          <div>
            <h4 className="text-white">
              <Translate contentKey="home.logged.message" interpolate={{ username: account.login }}>
                You are logged in as user {account.login}.
              </Translate>
            </h4>
            <Button tag={Link} to="/logout" color="light" size="lg" className="mt-3">
              <Translate contentKey="global.menu.account.logout">Sign out</Translate>
            </Button>
          </div>
        ) : (
          <div>
            <Button tag={Link} to="/login" color="light" size="lg" className="me-3">
              <Translate contentKey="global.menu.account.login">Sign in</Translate>
            </Button>
            <Button tag={Link} to="/account/register" color="secondary" size="lg">
              <Translate contentKey="global.menu.account.register">Register</Translate>
            </Button>
          </div>
        )}
      </div>

      <div className="text-center mt-5 mb-4">
        <h2>
          <Translate contentKey="home.sports.title">Our Sports</Translate>
        </h2>
      </div>
      <Row className="sports-section">
        <Col md="3">
          <Link to="/court?sport=pickleball" style={{ textDecoration: 'none' }}>
            <Card className="sport-card">
              <img src="content/images/pickleball.jpg" alt="Pickleball" className="sport-image" />
              <CardTitle tag="h5" className="sport-name">
                <Translate contentKey="home.sports.pickleball">Pickleball</Translate>
              </CardTitle>
            </Card>
          </Link>
        </Col>
        <Col md="3">
          <Link to="/court?sport=badminton" style={{ textDecoration: 'none' }}>
            <Card className="sport-card">
              <img src="content/images/badminton.jpg" alt="Badminton" className="sport-image" />
              <CardTitle tag="h5" className="sport-name">
                <Translate contentKey="home.sports.badminton">Badminton</Translate>
              </CardTitle>
            </Card>
          </Link>
        </Col>
        <Col md="3">
          <Link to="/court?sport=basketball" style={{ textDecoration: 'none' }}>
            <Card className="sport-card">
              <img src="content/images/basketball.jpg" alt="Basketball" className="sport-image" />
              <CardTitle tag="h5" className="sport-name">
                <Translate contentKey="home.sports.basketball">Basketball</Translate>
              </CardTitle>
            </Card>
          </Link>
        </Col>
        <Col md="3">
          <Link to="/court?sport=futsal" style={{ textDecoration: 'none' }}>
            <Card className="sport-card">
              <img src="content/images/futsal.jpg" alt="Futsal" className="sport-image" />
              <CardTitle tag="h5" className="sport-name">
                <Translate contentKey="home.sports.futsal">Futsal</Translate>
              </CardTitle>
            </Card>
          </Link>
        </Col>
      </Row>

      <footer className="text-center mt-5">
        <p>
          <Translate contentKey="home.like">If you like CourtEaseYK, do not forget to give us a star on</Translate>{' '}
          <a href="https://github.com/jhipster/generator-jhipster" target="_blank" rel="noopener noreferrer">
            GitHub
          </a>
          !
        </p>
        <p>&copy; 2025 CourtEaseYK. All rights reserved.</p>
      </footer>
    </div>
  );
};

export default Home;
