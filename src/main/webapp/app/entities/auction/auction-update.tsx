import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getUserLites } from 'app/entities/user-lite/user-lite.reducer';
import { AuctionStatus } from 'app/shared/model/enumerations/auction-status.model';
import { createEntity, getEntity, reset, updateEntity } from './auction.reducer';

export const AuctionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userLites = useAppSelector(state => state.userLite.entities);
  const auctionEntity = useAppSelector(state => state.auction.entity);
  const loading = useAppSelector(state => state.auction.loading);
  const updating = useAppSelector(state => state.auction.updating);
  const updateSuccess = useAppSelector(state => state.auction.updateSuccess);
  const auctionStatusValues = Object.keys(AuctionStatus);

  const handleClose = () => {
    navigate(`/auction${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUserLites({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.startingPrice !== undefined && typeof values.startingPrice !== 'number') {
      values.startingPrice = Number(values.startingPrice);
    }
    if (values.currentPrice !== undefined && typeof values.currentPrice !== 'number') {
      values.currentPrice = Number(values.currentPrice);
    }
    values.startDate = convertDateTimeToServer(values.startDate);
    values.endDate = convertDateTimeToServer(values.endDate);
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);
    values.deletedDate = convertDateTimeToServer(values.deletedDate);

    const entity = {
      ...auctionEntity,
      ...values,
      creator: userLites.find(it => it.id.toString() === values.creator?.toString()),
      winner: userLites.find(it => it.id.toString() === values.winner?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          startDate: displayDefaultDateTime(),
          endDate: displayDefaultDateTime(),
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
          deletedDate: displayDefaultDateTime(),
        }
      : {
          auctionStatus: 'ACTIVE',
          ...auctionEntity,
          startDate: convertDateTimeFromServer(auctionEntity.startDate),
          endDate: convertDateTimeFromServer(auctionEntity.endDate),
          createdDate: convertDateTimeFromServer(auctionEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(auctionEntity.lastModifiedDate),
          deletedDate: convertDateTimeFromServer(auctionEntity.deletedDate),
          creator: auctionEntity?.creator?.id,
          winner: auctionEntity?.winner?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.auction.home.createOrEditLabel" data-cy="AuctionCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.auction.home.createOrEditLabel">Create or edit a Auction</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="auction-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.auction.title')}
                id="auction-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.auction.description')}
                id="auction-description"
                name="description"
                data-cy="description"
                type="textarea"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.auction.startingPrice')}
                id="auction-startingPrice"
                name="startingPrice"
                data-cy="startingPrice"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.auction.currentPrice')}
                id="auction-currentPrice"
                name="currentPrice"
                data-cy="currentPrice"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.auction.startDate')}
                id="auction-startDate"
                name="startDate"
                data-cy="startDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.auction.endDate')}
                id="auction-endDate"
                name="endDate"
                data-cy="endDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.auction.auctionStatus')}
                id="auction-auctionStatus"
                name="auctionStatus"
                data-cy="auctionStatus"
                type="select"
              >
                {auctionStatusValues.map(auctionStatus => (
                  <option value={auctionStatus} key={auctionStatus}>
                    {translate(`monsterdamChicksApp.AuctionStatus.${auctionStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('monsterdamChicksApp.auction.createdDate')}
                id="auction-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.auction.lastModifiedDate')}
                id="auction-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.auction.createdBy')}
                id="auction-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.auction.lastModifiedBy')}
                id="auction-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.auction.deletedDate')}
                id="auction-deletedDate"
                name="deletedDate"
                data-cy="deletedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="auction-creator"
                name="creator"
                data-cy="creator"
                label={translate('monsterdamChicksApp.auction.creator')}
                type="select"
              >
                <option value="" key="0" />
                {userLites
                  ? userLites.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="auction-winner"
                name="winner"
                data-cy="winner"
                label={translate('monsterdamChicksApp.auction.winner')}
                type="select"
              >
                <option value="" key="0" />
                {userLites
                  ? userLites.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/auction" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default AuctionUpdate;
