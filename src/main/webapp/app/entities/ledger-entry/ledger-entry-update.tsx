import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { LedgerEntryType } from 'app/shared/model/enumerations/ledger-entry-type.model';
import { LedgerAccountType } from 'app/shared/model/enumerations/ledger-account-type.model';
import { LedgerReason } from 'app/shared/model/enumerations/ledger-reason.model';
import { createEntity, getEntity, reset, updateEntity } from './ledger-entry.reducer';

export const LedgerEntryUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const ledgerEntryEntity = useAppSelector(state => state.ledgerEntry.entity);
  const loading = useAppSelector(state => state.ledgerEntry.loading);
  const updating = useAppSelector(state => state.ledgerEntry.updating);
  const updateSuccess = useAppSelector(state => state.ledgerEntry.updateSuccess);
  const ledgerEntryTypeValues = Object.keys(LedgerEntryType);
  const ledgerAccountTypeValues = Object.keys(LedgerAccountType);
  const ledgerReasonValues = Object.keys(LedgerReason);

  const handleClose = () => {
    navigate(`/ledger-entry${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
    if (values.amount !== undefined && typeof values.amount !== 'number') {
      values.amount = Number(values.amount);
    }
    if (values.accountOwnerId !== undefined && typeof values.accountOwnerId !== 'number') {
      values.accountOwnerId = Number(values.accountOwnerId);
    }
    if (values.referenceId !== undefined && typeof values.referenceId !== 'number') {
      values.referenceId = Number(values.referenceId);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.deletedDate = convertDateTimeToServer(values.deletedDate);

    const entity = {
      ...ledgerEntryEntity,
      ...values,
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
          createdDate: displayDefaultDateTime(),
          deletedDate: displayDefaultDateTime(),
        }
      : {
          entryType: 'CREDIT',
          accountType: 'PLATFORM',
          reason: 'PURCHASE',
          ...ledgerEntryEntity,
          createdDate: convertDateTimeFromServer(ledgerEntryEntity.createdDate),
          deletedDate: convertDateTimeFromServer(ledgerEntryEntity.deletedDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.ledgerEntry.home.createOrEditLabel" data-cy="LedgerEntryCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.ledgerEntry.home.createOrEditLabel">Create or edit a LedgerEntry</Translate>
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
                  id="ledger-entry-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.ledgerEntry.amount')}
                id="ledger-entry-amount"
                name="amount"
                data-cy="amount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.ledgerEntry.currency')}
                id="ledger-entry-currency"
                name="currency"
                data-cy="currency"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 3, message: translate('entity.validation.maxlength', { max: 3 }) },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.ledgerEntry.entryType')}
                id="ledger-entry-entryType"
                name="entryType"
                data-cy="entryType"
                type="select"
              >
                {ledgerEntryTypeValues.map(ledgerEntryType => (
                  <option value={ledgerEntryType} key={ledgerEntryType}>
                    {translate(`monsterdamChicksApp.LedgerEntryType.${ledgerEntryType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('monsterdamChicksApp.ledgerEntry.accountType')}
                id="ledger-entry-accountType"
                name="accountType"
                data-cy="accountType"
                type="select"
              >
                {ledgerAccountTypeValues.map(ledgerAccountType => (
                  <option value={ledgerAccountType} key={ledgerAccountType}>
                    {translate(`monsterdamChicksApp.LedgerAccountType.${ledgerAccountType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('monsterdamChicksApp.ledgerEntry.accountOwnerId')}
                id="ledger-entry-accountOwnerId"
                name="accountOwnerId"
                data-cy="accountOwnerId"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.ledgerEntry.reason')}
                id="ledger-entry-reason"
                name="reason"
                data-cy="reason"
                type="select"
              >
                {ledgerReasonValues.map(ledgerReason => (
                  <option value={ledgerReason} key={ledgerReason}>
                    {translate(`monsterdamChicksApp.LedgerReason.${ledgerReason}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('monsterdamChicksApp.ledgerEntry.referenceType')}
                id="ledger-entry-referenceType"
                name="referenceType"
                data-cy="referenceType"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 40, message: translate('entity.validation.maxlength', { max: 40 }) },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.ledgerEntry.referenceId')}
                id="ledger-entry-referenceId"
                name="referenceId"
                data-cy="referenceId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.ledgerEntry.createdDate')}
                id="ledger-entry-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.ledgerEntry.createdBy')}
                id="ledger-entry-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.ledgerEntry.deletedDate')}
                id="ledger-entry-deletedDate"
                name="deletedDate"
                data-cy="deletedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ledger-entry" replace color="info">
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

export default LedgerEntryUpdate;
