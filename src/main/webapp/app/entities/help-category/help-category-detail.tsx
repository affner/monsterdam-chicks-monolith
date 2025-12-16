import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './help-category.reducer';

export const HelpCategoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const helpCategoryEntity = useAppSelector(state => state.helpCategory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="helpCategoryDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.helpCategory.detail.title">HelpCategory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{helpCategoryEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="monsterdamChicksApp.helpCategory.name">Name</Translate>
            </span>
          </dt>
          <dd>{helpCategoryEntity.name}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.helpCategory.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {helpCategoryEntity.createdDate ? (
              <TextFormat value={helpCategoryEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.helpCategory.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {helpCategoryEntity.lastModifiedDate ? (
              <TextFormat value={helpCategoryEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.helpCategory.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{helpCategoryEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.helpCategory.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{helpCategoryEntity.lastModifiedBy}</dd>
          <dt>
            <span id="deletedDate">
              <Translate contentKey="monsterdamChicksApp.helpCategory.deletedDate">Deleted Date</Translate>
            </span>
          </dt>
          <dd>
            {helpCategoryEntity.deletedDate ? (
              <TextFormat value={helpCategoryEntity.deletedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/help-category" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/help-category/${helpCategoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HelpCategoryDetail;
